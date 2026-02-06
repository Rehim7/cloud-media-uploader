# 1-ci mərhələ: Build mərhələsi
FROM gradle:8.5-jdk17-alpine AS build
WORKDIR /app

# Gradle fayllarını kopyalayırıq (Cache-dən istifadə etmək üçün)
COPY build.gradle settings.gradle ./
COPY src ./src

# Layihəni build edirik (Testləri keçirik ki, vaxt itkisi olmasın)
RUN gradle build -x test --no-daemon

# 2-ci mərhələ: Run mərhələsi (Yalnız JRE istifadə olunur)
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

# Build mərhələsindən yaranan JAR faylını kopyalayırıq
COPY --from=build /app/build/libs/app.jar app.jar

# Portu açırıq
EXPOSE 8080

# Tətbiqi başladırıq
ENTRYPOINT ["java", "-jar", "app.jar"]