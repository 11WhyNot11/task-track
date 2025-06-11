FROM eclipse-temurin:17-jdk-alpine

# Встановлюємо робочу директорію в контейнері
WORKDIR /app

# Копіюємо файли Gradle та зависимості, щоб скористатись кешем
COPY build.gradle settings.gradle gradlew /app/
COPY gradle /app/gradle

# Качаємо залежності (можна буде кешувати)
RUN ./gradlew dependencies --no-daemon

# Копіюємо увесь код проєкту
COPY . /app

# Будуємо проєкт
RUN ./gradlew bootJar --no-daemon

# Вказуємо, який jar запускати
CMD ["java", "-jar", "build/libs/task-tracker-api-0.0.1-SNAPSHOT.jar"]