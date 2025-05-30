# Flutter开发环境安装指南

## 系统要求

### Linux (Arch Linux)
- 操作系统: Arch Linux或其他Linux发行版
- 磁盘空间: 至少2.8GB (不包括IDE工具)
- 工具: bash, curl, file, git, mkdir, rm, unzip, which, xz-utils

## 安装步骤

### 1. 安装Flutter SDK

#### 方法一: 使用官方安装脚本 (推荐)
```bash
# 下载Flutter安装脚本
curl -fsSL https://storage.googleapis.com/flutter_infra_release/releases/stable/linux/flutter_linux_3.16.0-stable.tar.xz -o flutter.tar.xz

# 解压到指定目录
sudo mkdir -p /opt/flutter
sudo tar xf flutter.tar.xz -C /opt/flutter --strip-components=1

# 设置权限
sudo chmod -R 755 /opt/flutter
sudo chown -R $USER:$USER /opt/flutter
```

#### 方法二: 使用包管理器 (Arch Linux)
```bash
# 使用yay安装 (如果已安装AUR helper)
yay -S flutter

# 或使用pacman安装snap版本
sudo pacman -S snapd
sudo systemctl enable --now snapd.socket
sudo snap install flutter --classic
```

### 2. 配置环境变量

#### Fish Shell配置
```bash
# 编辑Fish配置文件
vim ~/.config/fish/config.fish

# 添加以下内容
set -gx PATH /opt/flutter/bin $PATH
set -gx ANDROID_HOME $HOME/Android/Sdk
set -gx PATH $ANDROID_HOME/cmdline-tools/latest/bin $PATH
set -gx PATH $ANDROID_HOME/platform-tools $PATH

# 重新加载配置
source ~/.config/fish/config.fish
```

#### Bash/Zsh配置 (如果使用其他Shell)
```bash
# 编辑.bashrc或.zshrc
echo 'export PATH="/opt/flutter/bin:$PATH"' >> ~/.bashrc
echo 'export ANDROID_HOME="$HOME/Android/Sdk"' >> ~/.bashrc
echo 'export PATH="$ANDROID_HOME/cmdline-tools/latest/bin:$PATH"' >> ~/.bashrc
echo 'export PATH="$ANDROID_HOME/platform-tools:$PATH"' >> ~/.bashrc

# 重新加载配置
source ~/.bashrc
```

### 3. 验证安装

```bash
# 检查Flutter版本
flutter --version

# 运行Flutter医生检查依赖
flutter doctor
```

### 4. 安装Android开发环境

#### 安装Android Studio
```bash
# Arch Linux
yay -S android-studio

# 或下载官方版本
wget https://dl.google.com/dl/android/studio/ide-zips/2023.1.1.28/android-studio-2023.1.1.28-linux.tar.gz
tar -xzf android-studio-*-linux.tar.gz
sudo mv android-studio /opt/
sudo ln -sf /opt/android-studio/bin/studio.sh /usr/local/bin/android-studio
```

#### 配置Android SDK
1. 启动Android Studio
2. 完成初始设置向导
3. 安装必要的SDK组件:
   - Android SDK Build-Tools
   - Android SDK Platform-Tools
   - Android SDK Tools
   - Android API 33 (或最新版本)

#### 创建Android虚拟设备 (AVD)
```bash
# 列出可用的系统镜像
avdmanager list targets

# 创建虚拟设备
avdmanager create avd -n flutter_emulator -k "system-images;android-33;google_apis;x86_64"

# 启动模拟器
emulator -avd flutter_emulator
```

### 5. 安装其他必要工具

#### 安装必要的系统包
```bash
# Arch Linux
sudo pacman -S git curl unzip lib32-glibc

# 安装Chrome (用于Web开发)
yay -S google-chrome
```

#### 安装VS Code和Flutter插件 (可选)
```bash
# 安装VS Code
yay -S visual-studio-code-bin

# 启动VS Code并安装Flutter插件
code --install-extension Dart-Code.flutter
code --install-extension Dart-Code.dart-code
```

### 6. 验证完整安装

```bash
# 运行完整的Flutter doctor检查
flutter doctor -v

# 期望看到类似输出:
# [✓] Flutter (Channel stable, 3.16.0, on Linux, locale en_US.UTF-8)
# [✓] Android toolchain - develop for Android devices
# [✓] Chrome - develop for the web
# [✓] Linux toolchain - develop for Linux desktop
# [✓] Android Studio (version 2023.1)
# [✓] VS Code (version 1.84)
# [✓] Connected device (1 available)
# [✓] HTTP Host Availability
```

### 7. 创建并运行测试项目

```bash
# 创建新的Flutter项目
flutter create test_app
cd test_app

# 检查可用设备
flutter devices

# 运行项目 (确保模拟器或设备已连接)
flutter run
```

## 常见问题解决

### 1. Flutter doctor显示Android许可证未接受
```bash
# 接受所有Android许可证
flutter doctor --android-licenses
```

### 2. 找不到Android SDK
```bash
# 设置Android SDK路径
flutter config --android-sdk $HOME/Android/Sdk
```

### 3. 模拟器启动失败
```bash
# 确保虚拟化已启用
# 检查BIOS设置中的Intel VT-x或AMD-V

# 安装KVM支持 (Linux)
sudo pacman -S qemu libvirt bridge-utils dnsmasq
sudo systemctl enable libvirtd
sudo usermod -a -G libvirt $USER
```

### 4. 权限问题
```bash
# 修复Flutter目录权限
sudo chown -R $USER:$USER /opt/flutter
sudo chmod -R 755 /opt/flutter
```

### 5. 网络连接问题
```bash
# 如果在中国大陆，可能需要配置镜像
export FLUTTER_STORAGE_BASE_URL=https://storage.flutter-io.cn
export PUB_HOSTED_URL=https://pub.flutter-io.cn

# 添加到shell配置文件中使其永久生效
```

## IDE配置推荐

### Android Studio配置
1. 安装Flutter和Dart插件
2. 配置模拟器设备
3. 设置代码格式化规则
4. 启用热重载

### VS Code配置
```json
// settings.json
{
  "dart.flutterSdkPath": "/opt/flutter",
  "dart.checkForSdkUpdates": false,
  "dart.previewFlutterUiGuides": true,
  "dart.previewFlutterUiGuidesCustomTracking": true
}
```

## 性能优化建议

1. **启用热重载**: 提高开发效率
2. **使用profile模式**: 性能分析时使用 `flutter run --profile`
3. **禁用不必要的动画**: 开发时可以加快构建速度
4. **使用真机测试**: 最终测试时使用真实设备

## 更新Flutter

```bash
# 更新Flutter到最新稳定版
flutter upgrade

# 检查更新后的状态
flutter doctor
```

---

安装完成后，您就可以开始ToComplete移动端的开发了！🚀 