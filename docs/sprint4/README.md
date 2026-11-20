# ШопЛайн (ShopLine) – Автоматизація тестування (Спринт 4)

**Команда:** ФБ-44  
**Тема:** Автоматизація тестування бізнес-логіки та веб-інтерфейсу (JUnit 5 + Selenium WebDriver)  
**Тестований модуль:** `ua.kpi.tzyapz:shopline-module` (`v1`) – кошик, знижки за обсяг, доставка та промокоди  
**Тестовий UI-стенд:** https://practicesoftwaretesting.com

---

## 1. Вимоги до середовища (Prerequisites)

Для збірки проєкту та запуску автотестів «з нуля» на машині мають бути встановлені:
- **Java Development Kit (JDK):** версія **17** або вище (з налаштованою змінною `JAVA_HOME`).
- **Apache Maven:** версія **3.9+** (доданий у системний `PATH`).
- **Браузер:** Google Chrome / Chromium актуальної версії (драйвер керується автоматично через Selenium Manager; тести запускаються у режимі `--headless=new`).
- **Git** та доступ до інтернету (для завантаження Maven-залежностей і проходження UI-тестів на зовнішньому стенді).

---

## 2. Структура репозиторію

```text
shopline-fb44/
├── docs/
│   └── sprint4/
│       ├── README.md             # Інструкція збірки та запуску автотестів (Богдан Алферов)
│       ├── audit.md              # Протокол аудиту та перевірки DoD (Ярослав Мулявка)
│       ├── coverage-matrix.md    # Матриця покриття BL-вимог юніт-тестами (Богдан Барановський)
│       ├── retro.md              # Протокол ретроспективи (Олександр Шандра)
│       ├── surefire-summary.md   # Зведений протокол виконання тестів Surefire (Богдан Алферов)
│       ├── task-board.md         # Таблиця розподілу задач спринту (Олександр Шандра)
│       └── ui-scenarios.md       # Опис UI-сценаріїв та локаторів (Богдан Барановський)
├── selenium-ide/
│   ├── export/                   # Експортовані чернетки Java-коду із Selenium IDE
│   │   ├── AddToCartTest.java
│   │   └── SearchHammerTest.java
│   └── shopline.side             # Записані сценарії у форматі Selenium IDE
├── src/
│   ├── main/java/ua/kpi/tzyapz/shopline/
│   │   ├── Cart.java
│   │   ├── DiscountCalculator.java
│   │   ├── PromoCode.java
│   │   └── PromoCodeValidator.java
│   └── test/java/ua/kpi/tzyapz/shopline/
│       ├── CartTest.java
│       ├── DiscountCalculatorTest.java
│       ├── PromoCodeTest.java
│       ├── PromoCodeValidatorTest.java
│       └── ui/
│           ├── CartUiTest.java
│           ├── CheckoutUiTest.java
│           ├── LoginUiTest.java
│           └── SearchUiTest.java
├── .gitignore                    # Виключення target/, .idea/ та системних файлів із Git
└── pom.xml                       # Конфігурація Maven, залежності JUnit 5, Selenium та Surefire
```

---

## 3. Інструкція збірки та запуску тестів

### Крок 1. Клонування репозиторію
```bash
git clone [https://github.com/BodiaBrn/shopline-fb44.git](https://github.com/BodiaBrn/shopline-fb44.git)
cd shopline-fb44
```

### Крок 2. Запуск модульних тестів (Unit Tests – JUnit 5)
За замовчуванням конфігурація `maven-surefire-plugin` виключає тести з тегом `@Tag("ui")`, тому виконуються лише швидкі модульні тести бізнес-логіки:
```bash
mvn clean test
```
- **Очікуваний результат:** `Tests run: 28, Failures: 5, Errors: 0, Skipped: 0`.  
- **Примітка щодо статусу `BUILD FAILURE`:** Падіння 5 юніт-тестів є очікуваним і підтверджує виявлення закладених дефектів модуля `v1` (`BUG-MOD-001` – `BUG-MOD-005`, Issues `#13`–`#17`), зафіксованих у GitHub Issues з міткою `module` та у протоколі `surefire-summary.md`.

### Крок 3. Запуск лише UI-тестів (Selenium WebDriver)
Щоб прогнати окремо 8 браузерних сценаріїв з тегом `@Tag("ui")`:
```bash
mvn test -Dgroups=ui -DexcludedGroups=""
```
- **Очікуваний результат:** `Tests run: 8, Failures: 0, Errors: 0, Skipped: 0` (`BUILD SUCCESS`).

### Крок 4. Повний прогін усіх тестів (Unit + UI)
Щоб запустити весь тестовий набір разом (28 юніт-прогонів + 8 UI-сценаріїв):
```bash
mvn test -DexcludedGroups=none
```
- **Очікуваний результат:** `Tests run: 36, Failures: 5, Errors: 0, Skipped: 0`.

---

## 4. Перегляд звітів Surefire та сценаріїв Selenium IDE

- **Звіти Maven Surefire:** Після запуску команд текстові та XML-звіти генеруються у директорії `target/surefire-reports/`. Зведений протокол збережено у `docs/sprint4/surefire-summary.md`.
- **Selenium IDE:** Для перегляду записаних сценаріїв відкрийте розширення **Selenium IDE** у браузері Chrome/Firefox, натисніть *Open an existing project* та виберіть файл `selenium-ide/shopline.side`.

---

## 5. Команда проєкту (ФБ-44)

| Роль | Учасник | Зона відповідальності у Спринті 4 |
|---|---|---|
| **Керівник тестування (Test Lead)** | Богдан Алферов | Декомпозиція, контроль нормативів, `README.md`, `surefire-summary.md`, демо-показ, юніт-тести |
| **Тест-аналітик (Test Analyst)** | Богдан Барановський | Матриця покриття (`coverage-matrix.md`), опис UI-сценаріїв (`ui-scenarios.md`), юніт-тести |
| **Інженер з тестування (SDET)** | Вадим Костюк | Конфігурація Maven/JDK, Selenium IDE (`.side` + експорт), UI WebDriver тести (`@Tag("ui")`), юніт-тести |
| **Скрам-майстер / Рецензент** | Олександр Шандра | Таблиця задач (`task-board.md`), Code Review, GitHub Issues (`module`), ретроспектива, юніт-тести |
| **Аудитор-опонент (Auditor)** | Ярослав Мулявка | Незалежна перевірка збірки «з нуля» (`audit.md`), перевірка DoD, питання замовника до демо, юніт-тести |
