import 'package:flutter/material.dart';
import 'package:flutter_bloc/flutter_bloc.dart';
import 'package:front/bloc/card/card_bloc.dart';

class CardView extends StatelessWidget {
  const CardView({super.key});

  @override
  Widget build(BuildContext context) {
    final cardBloc = BlocProvider.of<CardBloc>(context);
    cardBloc.add(LoadCardEvent());
    return Center(
        child: BlocBuilder<CardBloc, CardState>(
            bloc: cardBloc,
            builder: (context, state) {
              return const Text("data");
            }));
  }
}
