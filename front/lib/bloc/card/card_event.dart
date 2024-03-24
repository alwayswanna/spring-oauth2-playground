part of 'card_bloc.dart';

@immutable
sealed class CardEvent {}

class LoadCardEvent extends CardEvent {}
class UnLoadCardEvent extends CardEvent {}
