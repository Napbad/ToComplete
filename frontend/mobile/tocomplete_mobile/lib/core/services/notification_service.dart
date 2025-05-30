import 'package:flutter/foundation.dart';
import 'package:flutter_local_notifications/flutter_local_notifications.dart';
import 'package:permission_handler/permission_handler.dart';
import '../constants/app_constants.dart';

class NotificationService {
  static final FlutterLocalNotificationsPlugin _plugin = 
      FlutterLocalNotificationsPlugin();

  /// 初始化通知服务
  static Future<void> init() async {
    // Android初始化设置
    const AndroidInitializationSettings androidSettings =
        AndroidInitializationSettings('@mipmap/ic_launcher');

    // iOS初始化设置
    const DarwinInitializationSettings iosSettings =
        DarwinInitializationSettings(
      requestAlertPermission: true,
      requestBadgePermission: true,
      requestSoundPermission: true,
    );

    // 初始化设置
    const InitializationSettings initSettings = InitializationSettings(
      android: androidSettings,
      iOS: iosSettings,
    );

    // 初始化插件
    await _plugin.initialize(
      initSettings,
      onDidReceiveNotificationResponse: _onNotificationTapped,
    );

    // 请求权限
    await _requestPermissions();

    // 创建通知渠道（Android）
    await _createNotificationChannel();
  }

  /// 请求通知权限
  static Future<bool> _requestPermissions() async {
    if (defaultTargetPlatform == TargetPlatform.android) {
      final status = await Permission.notification.request();
      return status.isGranted;
    } else if (defaultTargetPlatform == TargetPlatform.iOS) {
      final result = await _plugin
          .resolvePlatformSpecificImplementation<
              IOSFlutterLocalNotificationsPlugin>()
          ?.requestPermissions(
            alert: true,
            badge: true,
            sound: true,
          );
      return result ?? false;
    }
    return true;
  }

  /// 创建通知渠道（Android）
  static Future<void> _createNotificationChannel() async {
    if (defaultTargetPlatform == TargetPlatform.android) {
      const AndroidNotificationChannel channel = AndroidNotificationChannel(
        AppConstants.notificationChannelId,
        AppConstants.notificationChannelName,
        description: AppConstants.notificationChannelDescription,
        importance: Importance.high,
        enableVibration: true,
        playSound: true,
      );

      await _plugin
          .resolvePlatformSpecificImplementation<
              AndroidFlutterLocalNotificationsPlugin>()
          ?.createNotificationChannel(channel);
    }
  }

  /// 通知点击处理
  static void _onNotificationTapped(NotificationResponse response) {
    // TODO: 处理通知点击事件，导航到相应页面
    print('Notification tapped: ${response.payload}');
  }

  /// 显示即时通知
  static Future<void> showNotification({
    required int id,
    required String title,
    required String body,
    String? payload,
  }) async {
    const AndroidNotificationDetails androidDetails = AndroidNotificationDetails(
      AppConstants.notificationChannelId,
      AppConstants.notificationChannelName,
      channelDescription: AppConstants.notificationChannelDescription,
      importance: Importance.high,
      priority: Priority.high,
      showWhen: true,
      icon: '@mipmap/ic_launcher',
    );

    const DarwinNotificationDetails iosDetails = DarwinNotificationDetails(
      presentAlert: true,
      presentBadge: true,
      presentSound: true,
    );

    const NotificationDetails details = NotificationDetails(
      android: androidDetails,
      iOS: iosDetails,
    );

    await _plugin.show(id, title, body, details, payload: payload);
  }

  /// 计划通知
  static Future<void> scheduleNotification({
    required int id,
    required String title,
    required String body,
    required DateTime scheduledDate,
    String? payload,
  }) async {
    const AndroidNotificationDetails androidDetails = AndroidNotificationDetails(
      AppConstants.notificationChannelId,
      AppConstants.notificationChannelName,
      channelDescription: AppConstants.notificationChannelDescription,
      importance: Importance.high,
      priority: Priority.high,
      showWhen: true,
      icon: '@mipmap/ic_launcher',
    );

    const DarwinNotificationDetails iosDetails = DarwinNotificationDetails(
      presentAlert: true,
      presentBadge: true,
      presentSound: true,
    );

    const NotificationDetails details = NotificationDetails(
      android: androidDetails,
      iOS: iosDetails,
    );

    await _plugin.schedule(
      id,
      title,
      body,
      scheduledDate,
      details,
      payload: payload,
      androidScheduleMode: AndroidScheduleMode.exactAllowWhileIdle,
    );
  }

  /// 重复通知
  static Future<void> scheduleRepeatingNotification({
    required int id,
    required String title,
    required String body,
    required RepeatInterval interval,
    String? payload,
  }) async {
    const AndroidNotificationDetails androidDetails = AndroidNotificationDetails(
      AppConstants.notificationChannelId,
      AppConstants.notificationChannelName,
      channelDescription: AppConstants.notificationChannelDescription,
      importance: Importance.high,
      priority: Priority.high,
      showWhen: true,
      icon: '@mipmap/ic_launcher',
    );

    const DarwinNotificationDetails iosDetails = DarwinNotificationDetails(
      presentAlert: true,
      presentBadge: true,
      presentSound: true,
    );

    const NotificationDetails details = NotificationDetails(
      android: androidDetails,
      iOS: iosDetails,
    );

    await _plugin.periodicallyShow(
      id,
      title,
      body,
      interval,
      details,
      payload: payload,
      androidScheduleMode: AndroidScheduleMode.exactAllowWhileIdle,
    );
  }

  /// 取消通知
  static Future<void> cancelNotification(int id) async {
    await _plugin.cancel(id);
  }

  /// 取消所有通知
  static Future<void> cancelAllNotifications() async {
    await _plugin.cancelAll();
  }

  /// 获取待处理的通知
  static Future<List<PendingNotificationRequest>> getPendingNotifications() async {
    return await _plugin.pendingNotificationRequests();
  }

  /// 显示备忘录提醒通知
  static Future<void> showMemoReminder({
    required int memoId,
    required String title,
    required String content,
    DateTime? dueTime,
  }) async {
    final notificationId = memoId + 1000; // 避免ID冲突
    final body = content.isNotEmpty ? content : '备忘录提醒';
    
    if (dueTime != null && dueTime.isAfter(DateTime.now())) {
      await scheduleNotification(
        id: notificationId,
        title: '📝 $title',
        body: body,
        scheduledDate: dueTime,
        payload: 'memo:$memoId',
      );
    } else {
      await showNotification(
        id: notificationId,
        title: '📝 $title',
        body: body,
        payload: 'memo:$memoId',
      );
    }
  }

  /// 显示计划提醒通知
  static Future<void> showPlanReminder({
    required int planId,
    required String title,
    required String description,
    DateTime? startTime,
  }) async {
    final notificationId = planId + 2000; // 避免ID冲突
    final body = description.isNotEmpty ? description : '计划提醒';
    
    if (startTime != null && startTime.isAfter(DateTime.now())) {
      await scheduleNotification(
        id: notificationId,
        title: '📅 $title',
        body: body,
        scheduledDate: startTime,
        payload: 'plan:$planId',
      );
    } else {
      await showNotification(
        id: notificationId,
        title: '📅 $title',
        body: body,
        payload: 'plan:$planId',
      );
    }
  }

  /// 显示每日总结通知
  static Future<void> showDailySummary({
    required int completedTasks,
    required int totalTasks,
  }) async {
    const notificationId = 9999;
    final title = '今日总结';
    final body = '您今天完成了 $completedTasks/$totalTasks 个任务，继续保持！';
    
    await showNotification(
      id: notificationId,
      title: title,
      body: body,
      payload: 'daily_summary',
    );
  }

  /// 计划每日总结通知
  static Future<void> scheduleDailySummary({
    required int hour,
    required int minute,
  }) async {
    const notificationId = 9998;
    final now = DateTime.now();
    var scheduledDate = DateTime(now.year, now.month, now.day, hour, minute);
    
    // 如果时间已过，安排到明天
    if (scheduledDate.isBefore(now)) {
      scheduledDate = scheduledDate.add(const Duration(days: 1));
    }
    
    await scheduleNotification(
      id: notificationId,
      title: '每日总结',
      body: '查看您今天的任务完成情况',
      scheduledDate: scheduledDate,
      payload: 'daily_summary_scheduled',
    );
  }

  /// 检查通知权限状态
  static Future<bool> hasNotificationPermission() async {
    if (defaultTargetPlatform == TargetPlatform.android) {
      return await Permission.notification.isGranted;
    } else if (defaultTargetPlatform == TargetPlatform.iOS) {
      final result = await _plugin
          .resolvePlatformSpecificImplementation<
              IOSFlutterLocalNotificationsPlugin>()
          ?.checkPermissions();
      return result?.isEnabled ?? false;
    }
    return true;
  }

  /// 打开系统通知设置
  static Future<void> openNotificationSettings() async {
    await openAppSettings();
  }
} 