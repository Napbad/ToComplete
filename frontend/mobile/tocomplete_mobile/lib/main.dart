import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:provider/provider.dart';
import 'package:hive_flutter/hive_flutter.dart';
import 'package:flutter_local_notifications/flutter_local_notifications.dart';

import 'core/constants/app_constants.dart';
import 'core/services/notification_service.dart';
import 'core/services/storage_service.dart';
import 'core/utils/app_router.dart';
import 'core/utils/app_theme.dart';
import 'presentation/bloc/auth/auth_bloc.dart';
import 'presentation/bloc/memo/memo_bloc.dart';
import 'presentation/bloc/plan/plan_bloc.dart';
import 'data/repositories/auth_repository_impl.dart';
import 'data/repositories/memo_repository_impl.dart';
import 'data/repositories/plan_repository_impl.dart';

void main() async {
  WidgetsFlutterBinding.ensureInitialized();
  
  // 初始化Hive本地存储
  await Hive.initFlutter();
  await StorageService.init();
  
  // 初始化通知服务
  await NotificationService.init();
  
  // 设置状态栏样式
  SystemChrome.setSystemUIOverlayStyle(
    const SystemUiOverlayStyle(
      statusBarColor: Colors.transparent,
      statusBarIconBrightness: Brightness.dark,
      systemNavigationBarColor: Colors.white,
      systemNavigationBarIconBrightness: Brightness.dark,
    ),
  );
  
  runApp(const ToCompleteApp());
}

class ToCompleteApp extends StatelessWidget {
  const ToCompleteApp({super.key});

  @override
  Widget build(BuildContext context) {
    return MultiProvider(
      providers: [
        // 创建数据仓库实例
        Provider<AuthRepositoryImpl>(
          create: (_) => AuthRepositoryImpl(),
        ),
        Provider<MemoRepositoryImpl>(
          create: (_) => MemoRepositoryImpl(),
        ),
        Provider<PlanRepositoryImpl>(
          create: (_) => PlanRepositoryImpl(),
        ),
        
        // 创建BLoC实例
        ChangeNotifierProvider<AuthBloc>(
          create: (context) => AuthBloc(
            authRepository: context.read<AuthRepositoryImpl>(),
          ),
        ),
        ChangeNotifierProvider<MemoBloc>(
          create: (context) => MemoBloc(
            memoRepository: context.read<MemoRepositoryImpl>(),
          ),
        ),
        ChangeNotifierProvider<PlanBloc>(
          create: (context) => PlanBloc(
            planRepository: context.read<PlanRepositoryImpl>(),
          ),
        ),
      ],
      child: Consumer<AuthBloc>(
        builder: (context, authBloc, child) {
          return MaterialApp.router(
            title: AppConstants.appName,
            debugShowCheckedModeBanner: false,
            theme: AppTheme.lightTheme,
            darkTheme: AppTheme.darkTheme,
            themeMode: ThemeMode.system,
            routerConfig: AppRouter.router,
            builder: (context, child) {
              // 响应式设计适配
              return MediaQuery(
                data: MediaQuery.of(context).copyWith(
                  textScaleFactor: MediaQuery.of(context).textScaleFactor.clamp(0.8, 1.2),
                ),
                child: child!,
              );
            },
          );
        },
      ),
    );
  }
} 