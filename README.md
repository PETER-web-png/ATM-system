# ATM系统 - 前端交互界面版

## 快速启动（推荐）

### 方式1：双击运行
直接双击 `start.bat` 文件，系统会自动下载Maven并启动

### 方式2：命令行运行
```bash
.\start.bat
```

## 访问系统

### 本地访问
启动成功后，打开浏览器访问：
- **本机访问**: http://localhost:8080

### 公网访问（让其他人也能访问）

已配置好 ngrok 内网穿透，直接使用以下链接：

**https://reputably-quaking-stubbly.ngrok-free.dev**

将此链接发给其他人即可访问ATM系统。

> 注意：重启 ngrok 后链接会变，需重新启动：
> ```bash
> ngrok http 8080
> ```

### 查看本机IP
在命令提示符输入 `ipconfig`，找到 IPv4 地址（如 192.168.x.x）

## 功能特性

- 用户注册/登录
- 查询账户信息
- 存款/取款
- 转账
- 修改密码
- 注销账户
- 响应式Bootstrap界面

## 项目结构

```
├── start.bat              # 启动脚本（自动下载Maven）
├── pom.xml               # Maven配置
├── src/main/java/com/atm/
│   ├── AtmApplication.java
│   ├── entity/Account.java
│   ├── repository/AccountRepository.java
│   ├── service/AtmService.java
│   └── controller/AtmController.java
└── src/main/resources/templates/
    ├── login.html
    ├── register.html
    ├── main.html
    ├── account.html
    ├── deposit.html
    ├── withdraw.html
    ├── transfer.html
    └── change-password.html
```

## 注意事项

1. 首次运行需要联网下载Maven（约10MB）
2. 需要Java JDK 17+环境
3. 需要安装MySQL并创建数据库：
```sql
CREATE DATABASE atmdb CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```
