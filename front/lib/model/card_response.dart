class CardResponse {
  final String cardId;
  final String cardTitle;
  final String cardDescription;
  final String userId;
  final DateTime updateTime;

  CardResponse(this.cardId, this.cardTitle, this.cardDescription, this.userId,
      this.updateTime);

  static CardResponse fromJson(Map<String, dynamic> json) {
    return CardResponse(
        json['cardId'],
        json['cardTitle'],
        json['cardDescription'],
        json['userId'],
        DateTime.parse(json['updateTime']));
  }
}
