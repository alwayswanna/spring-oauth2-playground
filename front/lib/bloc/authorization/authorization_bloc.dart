import 'dart:convert';

import 'package:bloc/bloc.dart';
import 'package:flutter/cupertino.dart';
import 'package:flutter_bloc/flutter_bloc.dart';
import 'package:front/constant/constants.dart';
import 'package:openidconnect/openidconnect.dart';

part 'authorization_event.dart';
part 'authorization_state.dart';

class AuthorizationBloc extends Bloc<AuthorizationEvent, AuthorizationState> {
  final BuildContext context;

  AuthorizationBloc({required this.context}) : super(AuthorizationState()) {
    on<RequestAuthorizationEvent>(_onRequestAuthorizationEvent);
    on<LogoutAuthorizationEvent>(_onLogoutAuthorizationEvent);
  }

  _onRequestAuthorizationEvent(RequestAuthorizationEvent event,
      Emitter<AuthorizationState> emit) async {
    final currentAuthorization = state.authorization;
    /* if current state has actual authorization return */
    if (currentAuthorization != null && !_isExpired(currentAuthorization)) {
      return;
    }

    final updatedState = currentAuthorization != null &&
        _isExpired(currentAuthorization) ?
    await _refreshAuthorization(state) :
    await _authorize();

    emit(updatedState);
  }

  _onLogoutAuthorizationEvent(LogoutAuthorizationEvent event,
      Emitter<AuthorizationState> emit) async {
    await _logout();
    emit(AuthorizationState(authorization: null, isAuthorized: false));
  }

  /// Method check token on expired.
  bool _isExpired(AuthorizationResponse response) {
    final currentTime = DateTime.now();
    return currentTime.millisecondsSinceEpoch <
        response.expiresAt.millisecondsSinceEpoch;
  }

  /// Refresh access token with refresh token.
  Future<AuthorizationState> _refreshAuthorization(
      AuthorizationState state) async {
    final refreshToken = state.authorization?.refreshToken;

    if (refreshToken == null) {
      throw Exception("Nullable refresh token.");
    }

    final response = await OpenIdConnect.refreshToken(
        request: RefreshRequest(
            clientId: clientId,
            clientSecret: secret,
            scopes: scopes,
            refreshToken: refreshToken,
            configuration: await _getConfiguration()
        )
    );

    return AuthorizationState(authorization: response, isAuthorized: true);
  }

  /// Method fetch new access/refresh tokens.
  Future<AuthorizationState> _authorize() async {
    final request = await InteractiveAuthorizationRequest.create(
        clientId: clientId,
        clientSecret: secret,
        redirectUrl: redirectUri,
        scopes: scopes,
        configuration: await _getConfiguration(),
        autoRefresh: true);
    final response = await OpenIdConnect.authorizeInteractive(
        context: context, title: "Authorize", request: request);

    return AuthorizationState(authorization: response, isAuthorized: true);
  }

  _logout() async {
    final authorization = state.authorization;
    if (authorization != null) {
      await OpenIdConnect.logout(request: LogoutRequest(
          idToken: authorization.idToken,
          postLogoutRedirectUrl: postLogoutRedirectUri,
          configuration: await _getConfiguration()));
    }
  }

  /// Method fetch OPEN_ID configuration of authorization server.
  Future<OpenIdConfiguration> _getConfiguration() async {
    final json = await DefaultAssetBundle.of(context)
        .loadString("assets/data/openid-well-know.json");
    final Map<String, dynamic> wellKnow = jsonDecode(json);

    return OpenIdConfiguration.fromJson(wellKnow);
  }
}
