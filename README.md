# ShopList — Лабораторная работа №7 (тестирование және отладка)

Android (Kotlin) жобасы: Room + Retrofit + XML UI. Пакет: `com.example.shop`.

## Іске қосу
1. Android Studio-да **Open** арқылы осы папканы ашыңыз (JDK 17).
2. Gradle wrapper jar архивте жоқ. Терминалда бір рет: `gradle wrapper --gradle-version 8.7`
   (немесе Android Studio өзі Gradle-ды жүктейді). CI үшін `gradlew` файлдары репозиторийде болуы керек.
3. `app/src/main/java/com/example/shop/ShopApp.kt` ішіндегі `BASE_URL` мекенжайын өз API-ыңызбен ауыстырыңыз.
   Қолданба курс алынбаса, 1.0 коэффициентімен жұмыс істей береді.

## Файлдар және тапсырмалар
| Тапсырма | Файл |
|---|---|
| 1 Logcat/Timber | `ShopApp.kt`, `ReleaseTree.kt` |
| 2 Отладчик | `logic/PriceCalculator.kt` (қатені өзіңіз енгізіңіз: `>=` орнына `>`) |
| 3 Крэштер | Өз бетіңізше уақытша кодпен шақырыңыз (NPE, индекс, NetworkOnMainThread) |
| 4 Инспекторлар | Android Studio: Layout / Database / Network Inspector |
| 5 Unit-тесттер | `PriceCalculatorParamTest`, `PriceCalculatorTest`, `ProductValidatorTest` |
| 6 Мок | `OrderViewModelTest`, `FakeProductRepository` |
| 7 Robolectric | `PrefsRobolectricTest` |
| 8 Room/Желі | `ProductDaoTest`, `MigrationTest` (androidTest), `CurrencyApiTest` |
| 9 Espresso | `AddProductTest` + `util/EspressoIdling.kt` |
| 10 Page Object | `pages/MainPage.kt`, `AddProductPageObjectTest` |
| 11 Қолмен тест | тест-кейстер құжатта; monkey: `adb shell monkey -p com.example.shop -v 5000 --throttle 100 -s 42` |
| 12 JaCoCo | `./gradlew jacocoReport` → `app/build/reports/jacoco/jacocoReport/html/index.html` |
| 13 Профайлинг | StrictMode `ShopApp.kt`, LeakCanary (debug тәуелділік) |
| 14 Macrobenchmark | `benchmark/` модулі (`settings.gradle.kts`-те `include(":benchmark")` комментін алыңыз) |
| 15 CI | `.github/workflows/ci.yml` |

## Командалар
```
./gradlew testDebugUnitTest          # JVM тесттері (unit + Robolectric + MockWebServer)
./gradlew connectedDebugAndroidTest  # эмулятор/құрылғыдағы тесттер
./gradlew jacocoReport               # coverage есебі
./gradlew lintDebug ktlintCheck detekt
```

## Назар аударыңыз
- Код Android SDK жоқ ортада жазылды, жинап көрілмеді. Gradle sync/build кезінде шыққан қателерді (версиялар, импорттар) түзетіңіз.
- `app/schemas/.../1.json` миграция тесті үшін қолмен жазылған 1-нұсқа схемасы. Тест құлаған жағдайда 1-нұсқаны Room генерациялаған файлмен ауыстырыңыз.
- 9/10-тапсырма: Room Flow Espresso-ға көрінбейді, сондықтан UI-тест кейде құлауы мүмкін. Бұл «flaky» талдауы үшін жақсы мысал.
- Compose тесті бұл жобада жоқ (UI XML-де). Compose қолдансаңыз құжаттағы мысалды қолданыңыз.
