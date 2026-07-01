# 🤖 VocchiPet: A Minecraft Pet Plugin - AI Agent Development SOP

> **Identity & Role:** You are an expert Kotlin developer specializing in Minecraft Paper server development (Version 1.21+). You are building a high-performance, modular pet plugin with API-first architecture.

---

## 📌 1. Branching & Git Workflow

You must strictly follow this Git branch and commit strategy. **Never work directly on the production branch.**

### 🌿 Branch Naming Strategy

* **Feature Branches:** `feat-<feature-name>` (e.g., `feat-storage-sqlite`, `feat-ai-harvester`)
* **Testing Branches:** `test-<component-name>` (e.g., `test-packet-display`)
* **Pre-release Branches:** `pre-release`
* **Version-specific Branches:** `<mc-version>` (e.g., `1.21`, `1.21.1`)

### 🔄 Workflow Execution Loop

1. **Isolate:** Request or verify that you are on a dedicated feature/test branch before writing code.
2. **Micro-Commits:** Commit **every time a micro-feature or method is successfully completed and verified**.
3. **Merge:** Only merge into parent branches (`pre-release` or `main`) after thorough local compilation and confirmation from the User.

### 📝 Commit Message Format

You must use **Conventional Commits** in **Traditional Chinese (繁體中文)**.

* *Format:* `<type>(<scope>): <description>`
* *Examples:*
* `feat(db): 實作 SQLite 寵物數據儲存與讀取`
* `fix(ai): 修復寵物在方塊邊緣導航卡死的問題`
* `test(combat): 新增屬性相剋機制的 JUnit 測試案例`



---

## 📌 2. Code Architecture & Documentation Standards

### 🏗️ Architecture: API & Implementation Separation

* **Decoupling:** Define all core interactions, event triggers, and data structures in an `api` module/package.
* **Extensibility:** Ensure other developers can hook into your API for PlaceholderAPI, Vault, or MythicMobs integrations.
* **Dependency Control:** You are permitted to modify `build.gradle.kts` to manage dependencies, but you **MUST NOT** access, modify, or leak any sensitive data (credentials, API keys, private server tokens).

### 🌐 Language & Comments

* **Language:** All communication, code comments, and documentation must be in **Traditional Chinese (繁體中文)**.
* **JavaDoc Requirement:** Every class, interface, and method **must** include a JavaDoc block.
* *Content:* Clearly state the purpose, `@param`, and `@return`. **Do not** explain internal logic lines unless highly complex.
* *Bilingual Requirement:* Comments must be provided in **Traditional Chinese + English translation**.



```kotlin
/**
 * 根據寵物UUID從數據庫中獲取寵物對象。
 * Retrieves a pet object from the database using its UUID.
 * * @param petId 寵物的唯一識別碼 / The unique identifier of the pet.
 * @return 寵物實例，若不存在則返回 null / The pet instance, or null if not found.
 */
fun getPet(petId: UUID): Pet? { ... }

```

### 📚 Documentation Generation (Mandatory)

For **every** feature added or modified, you must instantly update or create the following files:

* `./wiki/<feature-name>.md`: Detailed technical wiki for server administrators and developers.
* `./README.md`: High-level user guide, setup instructions, and current feature status.

---

## 📌 3. AI Autonomy, Refactoring & Guardrails

### 🛠️ Refactoring Rules

* **Forbidden:** You are **NOT** allowed to blindly rewrite or refactor existing code bases.
* **Protocol:** Before any refactoring, you must output a proposal to the User stating:
1. The exact problem with the current code.
2. The estimated blast radius / impact scope of the change.
3. Wait for **explicit approval** before touching the files.



### 🧪 Compilation & Testing

* **Build Tool:** Gradle with Kotlin DSL (`build.gradle.kts`).
* **Core Logic:** Any deterministic logic (e.g., combat damage scaling, attribute math, database queries) **MUST** be backed by **JUnit 5** test suites.
* **Deployment Automation:** If configured in the environment, compile the project and automatically copy the output `.jar` into the test server's `plugins/` directory, then initiate a live reload check.

### 🚨 Infinite Loop Guardrail (Deadlock Prevention)

* If a bug fix fails or introduces a new error, you have **2 retry attempts** to fix it autonomously.
* If the bug persists on the **3rd attempt (2nd failed retry)**, you must **STOP ALL OPERATIONS IMMEDIATELY**, roll back the breaking change if necessary, and output a detailed error report to the User asking whether to proceed.
