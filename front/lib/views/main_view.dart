import 'package:flutter/material.dart';
import 'package:flutter_bloc/flutter_bloc.dart';
import 'package:front/views/card_view.dart';

import '../bloc/authorization/authorization_bloc.dart';

class MainView extends StatefulWidget {
  const MainView({super.key});

  @override
  State<StatefulWidget> createState() {
    return _MainViewState();
  }
}

class _MainViewState extends State<MainView> {
  int _selectedItemId = 0;

  static const List<Widget> _pages = <Widget>[
    Text("Please log in before you start."),
    CardView()
  ];

  void _onMenuItemTaped(int index) {
    setState(() {
      _selectedItemId = index;
    });
  }

  @override
  Widget build(BuildContext context) {
    final authorizationBloc = BlocProvider.of<AuthorizationBloc>(context);
    return BlocBuilder<AuthorizationBloc, AuthorizationState>(
      bloc: authorizationBloc,
      builder: (context, state) {
        return Scaffold(
            appBar: AppBar(
              backgroundColor: Theme.of(context).colorScheme.inversePrimary,
              title: const Text('spring-oauth2-playground'),
            ),
            drawer: Drawer(
              child: BlocBuilder<AuthorizationBloc, AuthorizationState>(
                bloc: authorizationBloc,
                builder: (context, state) {
                  return ListView(children: _getDrawerItems(state, context));
                },
              ),
            ),
            body: Center(child: _pages[_selectedItemId]),
            floatingActionButton: FloatingActionButton.extended(
                onPressed: () {
                  state.isAuthorized
                      ? authorizationBloc.add(LogoutAuthorizationEvent())
                      : authorizationBloc.add(RequestAuthorizationEvent());
                },
                tooltip: state.isAuthorized ? "Logout" : "Login",
                label: state.isAuthorized
                    ? const Text("Logout")
                    : const Text("Login"),
                icon: state.isAuthorized
                    ? const Icon(Icons.logout)
                    : const Icon(Icons.login)));
      },
    );
  }

  _getDrawerItems(AuthorizationState state, BuildContext context) {
    var menuItems = <Widget>[
      const DrawerHeader(
        decoration: BoxDecoration(
          color: Colors.deepPurple,
        ),
        child: Text('Menu', style: TextStyle(color: Colors.white)),
      )
    ];

    final authorization = state.authorization;
    if (authorization == null) {
      return menuItems;
    }

    menuItems.add(ListTile(
      title: const Text('Data'),
      onTap: () {
        _onMenuItemTaped(1);
        Navigator.pop(context);
      },
    ));

    return menuItems;
  }
}
