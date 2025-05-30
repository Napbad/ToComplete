class AppConstants {
  // 应用信息
  static const String appName = 'ToComplete';
  static const String appVersion = '1.0.0';
  static const String appDescription = '智能备忘录和计划管理助手';
  
  // API配置
  static const String baseUrl = 'http://localhost:8080/api';
  static const int connectTimeout = 30000;
  static const int receiveTimeout = 30000;
  
  // 存储键名
  static const String tokenKey = 'auth_token';
  static const String userKey = 'user_info';
  static const String settingsKey = 'app_settings';
  static const String themeKey = 'theme_mode';
  static const String languageKey = 'language';
  
  // 路由名称
  static const String splashRoute = '/splash';
  static const String onboardingRoute = '/onboarding';
  static const String loginRoute = '/login';
  static const String registerRoute = '/register';
  static const String homeRoute = '/home';
  static const String memoListRoute = '/memos';
  static const String memoDetailRoute = '/memo/:id';
  static const String planListRoute = '/plans';
  static const String planDetailRoute = '/plan/:id';
  static const String calendarRoute = '/calendar';
  static const String settingsRoute = '/settings';
  static const String profileRoute = '/profile';
  
  // 分页配置
  static const int pageSize = 20;
  static const int maxPageSize = 100;
  
  // 缓存配置
  static const int cacheMaxAge = 24 * 60 * 60 * 1000; // 24小时
  static const int maxCacheSize = 50 * 1024 * 1024; // 50MB
  
  // 通知配置
  static const String notificationChannelId = 'tocomplete_channel';
  static const String notificationChannelName = 'ToComplete Notifications';
  static const String notificationChannelDescription = '备忘录和计划提醒通知';
  
  // 地理位置配置
  static const double locationAccuracy = 100.0; // 100米精度
  static const int locationTimeout = 30; // 30秒超时
  
  // 文件上传配置
  static const int maxFileSize = 10 * 1024 * 1024; // 10MB
  static const List<String> allowedImageTypes = ['jpg', 'jpeg', 'png', 'gif'];
  static const List<String> allowedDocTypes = ['pdf', 'doc', 'docx', 'txt'];
  
  // 安全配置
  static const int maxLoginAttempts = 5;
  static const int lockoutDuration = 30 * 60; // 30分钟
  static const int sessionTimeout = 24 * 60 * 60; // 24小时
  
  // UI配置
  static const double borderRadius = 12.0;
  static const double cardElevation = 4.0;
  static const double appBarElevation = 0.0;
  
  // 动画配置
  static const int animationDuration = 300;
  static const int splashDuration = 2000;
  
  // 错误消息
  static const String networkError = '网络连接失败，请检查网络设置';
  static const String serverError = '服务器异常，请稍后重试';
  static const String unknownError = '未知错误，请稍后重试';
  static const String authError = '身份验证失败，请重新登录';
  static const String permissionError = '权限不足，请检查应用权限设置';
  
  // 正则表达式
  static const String emailRegex = r'^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$';
  static const String phoneRegex = r'^1[3-9]\d{9}$';
  static const String passwordRegex = r'^(?=.*[a-zA-Z])(?=.*\d)[a-zA-Z\d@$!%*?&]{6,20}$';
  
  // 默认值
  static const String defaultAvatar = 'assets/images/default_avatar.png';
  static const String defaultCover = 'assets/images/default_cover.png';
  static const String noDataImage = 'assets/images/no_data.png';
  static const String errorImage = 'assets/images/error.png';
} 