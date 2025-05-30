import 'package:flutter/material.dart';
import 'package:go_router/go_router.dart';
import '../constants/app_constants.dart';
import '../services/storage_service.dart';
import '../../presentation/pages/splash/splash_page.dart';
import '../../presentation/pages/auth/login_page.dart';
import '../../presentation/pages/auth/register_page.dart';
import '../../presentation/pages/home/home_page.dart';
import '../../presentation/pages/memo/memo_list_page.dart';
import '../../presentation/pages/memo/memo_detail_page.dart';
import '../../presentation/pages/plan/plan_list_page.dart';
import '../../presentation/pages/plan/plan_detail_page.dart';
import '../../presentation/pages/calendar/calendar_page.dart';
import '../../presentation/pages/settings/settings_page.dart';
import '../../presentation/pages/profile/profile_page.dart';

class AppRouter {
  static final GoRouter router = GoRouter(
    initialLocation: AppConstants.splashRoute,
    redirect: (context, state) async {
      final isAuthenticated = StorageService.hasToken();
      final isOnSplash = state.matchedLocation == AppConstants.splashRoute;
      final isOnAuth = state.matchedLocation == AppConstants.loginRoute ||
                      state.matchedLocation == AppConstants.registerRoute;

      // 如果在启动页，允许访问
      if (isOnSplash) {
        return null;
      }

      // 如果未认证且不在认证页面，重定向到登录页
      if (!isAuthenticated && !isOnAuth) {
        return AppConstants.loginRoute;
      }

      // 如果已认证且在认证页面，重定向到首页
      if (isAuthenticated && isOnAuth) {
        return AppConstants.homeRoute;
      }

      return null;
    },
    routes: [
      // 启动页
      GoRoute(
        path: AppConstants.splashRoute,
        builder: (context, state) => const SplashPage(),
      ),

      // 认证相关页面
      GoRoute(
        path: AppConstants.loginRoute,
        builder: (context, state) => const LoginPage(),
      ),
      GoRoute(
        path: AppConstants.registerRoute,
        builder: (context, state) => const RegisterPage(),
      ),

      // 主要页面
      GoRoute(
        path: AppConstants.homeRoute,
        builder: (context, state) => const HomePage(),
      ),

      // 备忘录相关页面
      GoRoute(
        path: AppConstants.memoListRoute,
        builder: (context, state) => const MemoListPage(),
      ),
      GoRoute(
        path: AppConstants.memoDetailRoute,
        builder: (context, state) {
          final id = int.tryParse(state.pathParameters['id'] ?? '');
          if (id == null) {
            return const Scaffold(
              body: Center(child: Text('Invalid memo ID')),
            );
          }
          return MemoDetailPage(memoId: id);
        },
      ),

      // 计划相关页面
      GoRoute(
        path: AppConstants.planListRoute,
        builder: (context, state) => const PlanListPage(),
      ),
      GoRoute(
        path: AppConstants.planDetailRoute,
        builder: (context, state) {
          final id = int.tryParse(state.pathParameters['id'] ?? '');
          if (id == null) {
            return const Scaffold(
              body: Center(child: Text('Invalid plan ID')),
            );
          }
          return PlanDetailPage(planId: id);
        },
      ),

      // 日历页面
      GoRoute(
        path: AppConstants.calendarRoute,
        builder: (context, state) => const CalendarPage(),
      ),

      // 设置页面
      GoRoute(
        path: AppConstants.settingsRoute,
        builder: (context, state) => const SettingsPage(),
      ),

      // 个人资料页面
      GoRoute(
        path: AppConstants.profileRoute,
        builder: (context, state) => const ProfilePage(),
      ),
    ],
    errorBuilder: (context, state) => Scaffold(
      appBar: AppBar(title: const Text('页面不存在')),
      body: Center(
        child: Column(
          mainAxisAlignment: MainAxisAlignment.center,
          children: [
            const Icon(
              Icons.error_outline,
              size: 64,
              color: Colors.grey,
            ),
            const SizedBox(height: 16),
            const Text(
              '抱歉，您访问的页面不存在',
              style: TextStyle(fontSize: 16),
            ),
            const SizedBox(height: 24),
            ElevatedButton(
              onPressed: () => context.go(AppConstants.homeRoute),
              child: const Text('返回首页'),
            ),
          ],
        ),
      ),
    ),
  );

  /// 导航到登录页面
  static void goToLogin(BuildContext context) {
    context.go(AppConstants.loginRoute);
  }

  /// 导航到注册页面
  static void goToRegister(BuildContext context) {
    context.go(AppConstants.registerRoute);
  }

  /// 导航到首页
  static void goToHome(BuildContext context) {
    context.go(AppConstants.homeRoute);
  }

  /// 导航到备忘录列表
  static void goToMemoList(BuildContext context) {
    context.go(AppConstants.memoListRoute);
  }

  /// 导航到备忘录详情
  static void goToMemoDetail(BuildContext context, int memoId) {
    context.go('/memo/$memoId');
  }

  /// 导航到计划列表
  static void goToPlanList(BuildContext context) {
    context.go(AppConstants.planListRoute);
  }

  /// 导航到计划详情
  static void goToPlanDetail(BuildContext context, int planId) {
    context.go('/plan/$planId');
  }

  /// 导航到日历页面
  static void goToCalendar(BuildContext context) {
    context.go(AppConstants.calendarRoute);
  }

  /// 导航到设置页面
  static void goToSettings(BuildContext context) {
    context.go(AppConstants.settingsRoute);
  }

  /// 导航到个人资料页面
  static void goToProfile(BuildContext context) {
    context.go(AppConstants.profileRoute);
  }

  /// 退出登录，清除认证状态并导航到登录页
  static void logout(BuildContext context) {
    StorageService.removeToken();
    StorageService.removeUserInfo();
    context.go(AppConstants.loginRoute);
  }

  /// 检查当前是否在指定路由
  static bool isCurrentRoute(BuildContext context, String route) {
    final currentLocation = GoRouterState.of(context).matchedLocation;
    return currentLocation == route;
  }

  /// 获取当前路由路径
  static String getCurrentRoute(BuildContext context) {
    return GoRouterState.of(context).matchedLocation;
  }

  /// 返回上一页
  static void goBack(BuildContext context) {
    if (context.canPop()) {
      context.pop();
    } else {
      context.go(AppConstants.homeRoute);
    }
  }

  /// 替换当前路由
  static void replaceWith(BuildContext context, String route) {
    context.pushReplacement(route);
  }
} 