# Online Mini Quiz - Kodlama Standartları Dokümanı (Coding Standards)

## 1. Amaç ve Kapsam
Bu doküman, **Online Mini Quiz** yazılım projesi kapsamında geliştirilecek tüm Java kaynak kodlarının ve veritabanı yapılarının okunabilir, sürdürülebilir ve tutarlı olması için uyulması gereken yazılım geliştirme standartlarını tanımlar.

## 2. İsimlendirme Kuralları (Naming Conventions)
* **Sınıf İsimleri (Classes):** `PascalCase` kullanılır (Örn: `QuizFrame`, `QuizService`, `QuizRepository`, `DBConnection`).
* **Metot ve Fonksiyon İsimleri:** `camelCase` kullanılır ve fiil ile başlar (Örn: `calculateScore()`, `getQuestions()`).
* **Değişken İsimleri:** `camelCase` kullanılır, anlamlı ve açıklayıcı seçilir (Örn: `totalScore`, `currentQuestionIndex`). Tek harfli (`a`, `x`) değişkenlerden kaçınılır.
* **Sabitler (Constants):** Tamamı büyük harf ve alt çizgi (`UPPER_SNAKE_CASE`) ile yazılır (Örn: `MAX_QUESTION_COUNT = 20`).
* **Veritabanı Tablo ve Sütun İsimleri:** Küçük harf ve alt çizgi (`snake_case`) kullanılır (Örn: `quiz_results`, `user_id`).

## 3. Kod Formatlama ve Girinti Yapısı
* **Girintileme (Indentation):** Her blok seviyesinde 4 boşluk (4 spaces) kullanılır.
* **Süslü Parantezler (`{ }`):** Açılış parantezi aynı satırın sonunda yer alır, tek satırlık `if/for` bloklarında dahi parantez kullanılır.
* **Satır Uzunluğu:** Bir kod satırı 100–120 karakteri geçmemelidir.

## 4. Yorum Satırları ve Dokümantasyon
* Her sınıfın ve karmaşık metodun üstüne amacını, parametrelerini ve dönüş değerini açıklayan Javadoc yorumu eklenir.
* Yalnızca karmaşık iş mantığını açıklamak için satır içi yorum kullanılır; kendini açıkça anlatan kodlara gereksiz yorum yazılmaz.

## 5. Hata Yönetimi (Exception Handling)
* Veritabanı bağlantıları (`DBConnection`) ve kullanıcı girişlerinde olası hatalar `try-catch` blokları ile yakalanır.
* `catch` blokları kesinlikle boş bırakılmaz; kullanıcıya anlaşılır hata mesajı gösterilir ve hata loglanır.

## 6. Sürüm Kontrol (Git & Commit) Kuralları
* Commit mesajlarının başına ilgili kategori etiketi eklenir (Örn: `[DEV] Quiz arayüzü kodlandı`, `[DOC] Kodlama standartları eklendi`, `[SQA] Birim testler eklendi`).
