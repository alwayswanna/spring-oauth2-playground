import 'dart:async';

import 'package:bloc/bloc.dart';
import 'package:meta/meta.dart';

import '../../model/card_response.dart';
import '../../repository/card_repository.dart';
import '../authorization/authorization_bloc.dart';

part 'card_event.dart';
part 'card_state.dart';

class CardBloc extends Bloc<CardEvent, CardState> {
  final CardRepository cardRepository;
  final AuthorizationBloc authorizationBloc;
  late final StreamSubscription streamSubscription;

  CardBloc(this.cardRepository, this.authorizationBloc) : super(CardState([])) {
    on<LoadCardEvent>(_onLoadCardEvent);
    on<UnLoadCardEvent>(_onUnloadCardEvent);
    streamSubscription = authorizationBloc.stream.listen(_onLogoutUnloadData);
  }

  @override
  Future<void> close() async  {
    streamSubscription.cancel();
    return super.close();
  }

  _onLoadCardEvent(LoadCardEvent event, Emitter<CardState> emit) async {
    cardRepository.accessToken = authorizationBloc.state.authorization!.accessToken;
    final cards = await cardRepository.loadAllCards();
    emit(CardState(cards));
  }

  _onUnloadCardEvent(UnLoadCardEvent event, Emitter<CardState> emit) {
    emit(CardState([]));
  }

  _onLogoutUnloadData(AuthorizationState state) {
    if (!state.isAuthorized || state.authorization == null) {
      add(UnLoadCardEvent());
      cardRepository.accessToken = '';
    }
  }
}
