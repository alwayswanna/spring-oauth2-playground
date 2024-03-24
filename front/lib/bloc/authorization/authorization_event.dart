part of 'authorization_bloc.dart';

@immutable
sealed class AuthorizationEvent {}

class RequestAuthorizationEvent extends AuthorizationEvent {}

class LogoutAuthorizationEvent extends AuthorizationEvent {}
