import 'dart:convert';
import 'dart:developer';

import 'package:http/http.dart' as http;

import '../model/card_response.dart';

const String cardUrl = "http://127.0.0.1:7071/api/v1/";

class CardRepository {
  String? _accessToken;

  set accessToken(String value) {
    _accessToken = value;
  }

  /// Fetch card with ID.
  Future<CardResponse> loadCardById(String id) async {
    final response = await http.get(
        Uri.parse('${cardUrl}card?id=$id'),
        headers: _headers()
    );

    return response.statusCode == 200
        ? CardResponse.fromJson(jsonDecode(utf8.decode(response.bodyBytes)))
        : throw Exception("Error while fetch data from API");
  }

  /// Fetch all cards.
  Future<List<CardResponse>> loadAllCards() async {
    log("ACCESS_TOKE: $_accessToken");
    final response = await http.get(
        Uri.parse('${cardUrl}cards'),
        headers: _headers()
    );
    log(json.decode(utf8.decode(response.bodyBytes)));
    return response.statusCode == 200
        ? json.decode(utf8.decode(response.bodyBytes))
        : throw Exception("Error while fetch data from API");
  }

  _headers() {
    return {
      "Authorization": "Bearer $_accessToken"
    };
  }
}
