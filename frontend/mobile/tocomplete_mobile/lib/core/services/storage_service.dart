import 'package:hive_flutter/hive_flutter.dart';
import 'package:shared_preferences/shared_preferences.dart';
import '../constants/app_constants.dart';

class StorageService {
  static late Box _userBox;
  static late Box _settingsBox;
  static late Box _cacheBox;
  static late SharedPreferences _prefs;

  /// 初始化存储服务
  static Future<void> init() async {
    // 初始化SharedPreferences
    _prefs = await SharedPreferences.getInstance();

    // 注册Hive适配器（如果有自定义模型）
    // Hive.registerAdapter(UserAdapter());
    // Hive.registerAdapter(MemoAdapter());

    // 打开Hive数据库
    _userBox = await Hive.openBox('user');
    _settingsBox = await Hive.openBox('settings');
    _cacheBox = await Hive.openBox('cache');
  }

  /// Token相关操作
  static Future<void> saveToken(String token) async {
    await _prefs.setString(AppConstants.tokenKey, token);
  }

  static String? getToken() {
    return _prefs.getString(AppConstants.tokenKey);
  }

  static Future<void> removeToken() async {
    await _prefs.remove(AppConstants.tokenKey);
  }

  static bool hasToken() {
    return _prefs.containsKey(AppConstants.tokenKey);
  }

  /// 用户信息操作
  static Future<void> saveUserInfo(Map<String, dynamic> userInfo) async {
    await _userBox.put(AppConstants.userKey, userInfo);
  }

  static Map<String, dynamic>? getUserInfo() {
    final data = _userBox.get(AppConstants.userKey);
    return data != null ? Map<String, dynamic>.from(data) : null;
  }

  static Future<void> removeUserInfo() async {
    await _userBox.delete(AppConstants.userKey);
  }

  /// 应用设置操作
  static Future<void> saveSetting(String key, dynamic value) async {
    await _settingsBox.put(key, value);
  }

  static T? getSetting<T>(String key, {T? defaultValue}) {
    return _settingsBox.get(key, defaultValue: defaultValue) as T?;
  }

  static Future<void> removeSetting(String key) async {
    await _settingsBox.delete(key);
  }

  /// 主题设置
  static Future<void> saveThemeMode(String themeMode) async {
    await saveSetting(AppConstants.themeKey, themeMode);
  }

  static String getThemeMode() {
    return getSetting(AppConstants.themeKey, defaultValue: 'system') ?? 'system';
  }

  /// 语言设置
  static Future<void> saveLanguage(String language) async {
    await saveSetting(AppConstants.languageKey, language);
  }

  static String getLanguage() {
    return getSetting(AppConstants.languageKey, defaultValue: 'zh') ?? 'zh';
  }

  /// 缓存操作
  static Future<void> saveCache(String key, dynamic data, {Duration? duration}) async {
    final expireTime = duration != null 
        ? DateTime.now().add(duration).millisecondsSinceEpoch
        : DateTime.now().add(const Duration(days: 7)).millisecondsSinceEpoch;

    await _cacheBox.put(key, {
      'data': data,
      'expireTime': expireTime,
    });
  }

  static T? getCache<T>(String key) {
    final cached = _cacheBox.get(key);
    if (cached == null) return null;

    final expireTime = cached['expireTime'] as int;
    if (DateTime.now().millisecondsSinceEpoch > expireTime) {
      _cacheBox.delete(key);
      return null;
    }

    return cached['data'] as T?;
  }

  static Future<void> removeCache(String key) async {
    await _cacheBox.delete(key);
  }

  static Future<void> clearCache() async {
    await _cacheBox.clear();
  }

  /// 清除所有数据
  static Future<void> clearAll() async {
    await _prefs.clear();
    await _userBox.clear();
    await _settingsBox.clear();
    await _cacheBox.clear();
  }

  /// 获取缓存大小
  static Future<int> getCacheSize() async {
    int size = 0;
    for (final key in _cacheBox.keys) {
      final value = _cacheBox.get(key);
      if (value != null) {
        size += value.toString().length;
      }
    }
    return size;
  }

  /// 清理过期缓存
  static Future<void> cleanExpiredCache() async {
    final now = DateTime.now().millisecondsSinceEpoch;
    final keysToDelete = <dynamic>[];

    for (final key in _cacheBox.keys) {
      final cached = _cacheBox.get(key);
      if (cached != null && cached is Map) {
        final expireTime = cached['expireTime'] as int?;
        if (expireTime != null && now > expireTime) {
          keysToDelete.add(key);
        }
      }
    }

    for (final key in keysToDelete) {
      await _cacheBox.delete(key);
    }
  }

  /// 备份数据
  static Future<Map<String, dynamic>> backupData() async {
    return {
      'user': _userBox.toMap(),
      'settings': _settingsBox.toMap(),
      'timestamp': DateTime.now().toIso8601String(),
    };
  }

  /// 恢复数据
  static Future<void> restoreData(Map<String, dynamic> backupData) async {
    if (backupData.containsKey('user')) {
      await _userBox.clear();
      await _userBox.putAll(Map<String, dynamic>.from(backupData['user']));
    }

    if (backupData.containsKey('settings')) {
      await _settingsBox.clear();
      await _settingsBox.putAll(Map<String, dynamic>.from(backupData['settings']));
    }
  }
} 