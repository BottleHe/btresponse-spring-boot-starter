# 发布到 Maven 中央仓库指南

本项目包含两个需要发布的构件（`test-project` 为演示工程，不发布）：

| 构件 | 说明 |
| --- | --- |
| `work.bottle.plugin:btresponse-core` | 异常与响应模型定义 |
| `work.bottle.plugin:btresponse-spring-boot-starter` | Spring Boot 自动装配starter，依赖 core |

> **背景**：旧的 OSSRH（`s01.oss.sonatype.org` + `nexus-staging-maven-plugin`）已于 2025-06-30 停服。
> 本项目已迁移到 **Sonatype Central Portal**（`central.sonatype.com`），使用 `central-publishing-maven-plugin` 发布。
> 两个模块的 pom 中已配置好该插件（`publishingServerId=central`、`autoPublish=true`）。

---

## 一、一次性准备

### 1. Central Portal 账号与命名空间

1. 访问 https://central.sonatype.com ，使用原有 OSSRH 账号登录（老账号已自动迁移），或用发布时使用的邮箱注册新账号。
2. 确认命名空间 `work.bottle.plugin` 已被当前账号验证（Namespace 页）。老 OSSRH 用户迁移后命名空间通常已绑定。

### 2. 配置 API Token

在 Portal 的 **Account → Generate User Token** 生成 token，写入 `~/.m2/settings.xml`：

```xml
<settings>
  <servers>
    <server>
      <id>central</id>   <!-- 必须与 pom 中 publishingServerId 一致 -->
      <username>生成的用户名</username>
      <password>生成的密码</password>
    </server>
  </servers>
</settings>
```

### 3. GPG 签名密钥

中央仓库要求所有构件有 GPG 签名，且**公钥必须上传到公钥服务器**。

```bash
# 查看本机密钥（注意 EXPRESSED 的 expired 标记，过期的密钥会导致签名校验失败）
gpg --list-secret-keys --keyid-format short

# 若密钥过期，续期一年（会提示输入密钥口令）：
gpg --quick-set-expire <KEYID> 1y

# 若没有密钥，新建（RSA 3072 以上）：
gpg --full-generate-key

# 上传公钥（Central 校验时需要能查到）：
gpg --keyserver keyserver.ubuntu.com --send-keys <KEYID>
```

若不想每次输入口令，可在 `settings.xml` 的 central server 中加 `<passphrase>` 子标签，或发布时用 `-Dgpg.passphrase=...`。

---

## 二、每次发布流程

### 1. 确定版本号

中央仓库的 release 版本**一经发布不可覆盖**。先确认版本未被占用：

```bash
curl -s https://repo1.maven.org/maven2/work/bottle/plugin/btresponse-spring-boot-starter/maven-metadata.xml | tail -8
curl -s https://repo1.maven.org/maven2/work/bottle/plugin/btresponse-core/maven-metadata.xml | tail -8
```

版本规则：

- `btresponse-core/pom.xml` 的 `<version>`
- `btresponse-spring-boot-starter/pom.xml` 的 `<version>` 与 `<btresponse-core.version>`（两者同步升级，core 只要没发过可以保持小步领先）

### 2. 记录变更

分别在 `btresponse-core/Version.md`、`btresponse-spring-boot-starter/Version.md` 中补充本次版本条目。

### 3. 构建并发布

```bash
# 全量验证（跑 test-project 的 89 个回归测试）
mvn clean install -Dgpg.skip=true

# 发布 core 和 starter（按 reactor 顺序先 core 后 starter，需要 GPG 口令）
mvn clean deploy -pl btresponse-core,btresponse-spring-boot-starter
```

插件配置了 `autoPublish=true`，上传完成后自动发布，无需再到 Portal 手动点确认。
若希望命令阻塞到中央仓库真正可下载为止，在两个 pom 的插件配置中加上 `<waitUntil>published</waitUntil>`（耗时几分钟）。

> 提示：`central-publishing-maven-plugin` 会把本次 deploy 的全部构件打成单个 bundle 上传，
> 所以 core 和 starter 放在同一条 deploy 命令里发布最方便。

### 4. 验证

- Portal 的 **Publishing → Deployments** 页面查看状态（应为 Published）
- 同步有延迟（一般几分钟到半小时），之后可下载验证：

```bash
curl -sI https://repo1.maven.org/maven2/work/bottle/plugin/btresponse-core/2.0.2/btresponse-core-2.0.2.pom | head -1
```

- https://central.sonatype.com/search?q=work.bottle.plugin 可搜索确认
- https://mvnrepository.com/artifact/work.bottle.plugin 收录会有数天延迟

### 5. Git 收尾

```bash
git tag btresponse-core-2.0.2 btresponse-spring-boot-starter-2.0.2   # 或统一 tag: v2.0.2
git push origin master --tags
```

---

## 三、常见问题

| 现象 | 原因与处理 |
| --- | --- |
| deploy 返回 401 | `settings.xml` 中 `<server><id>central</id>` 的 token 缺失或失效，重新生成 |
| `Missing signature` / 签名校验失败 | GPG 密钥过期（续期后需重新上传公钥）；或公钥未上传 keyserver |
| `version already exists` / staging 冲突 | 该版本已在中央仓库，升级版本号重来 |
| javadoc/source 校验失败 | 两个 pom 已固定 `maven-source-plugin` / `maven-javadoc-plugin`（`-Xdoclint:none`），不要移除 |
| 本地只想构建不发布 | 加 `-Dgpg.skip=true`，且用 `install` 而非 `deploy` |
