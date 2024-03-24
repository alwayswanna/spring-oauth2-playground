import 'package:flutter/material.dart';
import 'package:flutter_bloc/flutter_bloc.dart';
import 'package:front/bloc/authorization/authorization_bloc.dart';
import 'package:front/bloc/card/card_bloc.dart';
import 'package:front/repository/card_repository.dart';
import 'package:front/views/main_view.dart';

void main() {
  runApp(MultiBlocProvider(
    providers: [
      BlocProvider<AuthorizationBloc>(
          create: (context) => AuthorizationBloc(context: context)),
      BlocProvider<CardBloc>(
        create: (context) => CardBloc(
          CardRepository(), BlocProvider.of<AuthorizationBloc>(context)
        ),
      ),
    ],
    child: const App(),
  ));
}

class App extends StatelessWidget {
  const App({super.key});

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'Flutter Demo',
      theme: ThemeData(
        colorScheme: ColorScheme.fromSeed(seedColor: Colors.deepPurple),
        useMaterial3: true,
      ),
      routes: {'/authorized': (context) => const MainView()},
      home: const MainView(),
    );
  }
}
