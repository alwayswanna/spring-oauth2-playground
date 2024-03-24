part of 'authorization_bloc.dart';

class AuthorizationState {
  final AuthorizationResponse? authorization;
  final bool isAuthorized;

  AuthorizationState({this.authorization, this.isAuthorized = false});

  AuthorizationState copyWith(AuthorizationResponse? response) {
    return AuthorizationState(
      authorization: response,
      isAuthorized: response != null
    );
  }
}
