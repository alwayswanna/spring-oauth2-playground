import 'dart:convert';

import 'package:flutter/material.dart';
import 'package:openidconnect/openidconnect.dart';

const String clientId = "default-client";
const String secret = "ZGVmYXVsdC1jbGllbnQ=";
const String callbackUrl = "http://127.0.0.1:8000/callback.html";
const List<String> scopes = ["openid"];

class Auth {
  static Future<AuthorizationResponse?> authorize(BuildContext context) async {
    final json = await DefaultAssetBundle.of(context)
        .loadString("assets/data/openid-well-know.json");
    final Map<String, dynamic> wellKnow = jsonDecode(json);

    var interactiveAuthorizationRequest =
        await InteractiveAuthorizationRequest.create(
            clientId: clientId,
            clientSecret: secret,
            redirectUrl: callbackUrl,
            scopes: scopes,
            configuration: OpenIdConfiguration.fromJson(wellKnow),
            autoRefresh: true);

    return OpenIdConnect.authorizeInteractive(
      context: context,
      title: "Authorize",
      request: interactiveAuthorizationRequest,
    );
  }
}
