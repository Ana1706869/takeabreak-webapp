# Start Take a Break Webapp with diagnostic output
$java = 'C:\Program Files\Java\jdk-24\bin\java.exe'
$jar = 'C:\Users\anasi\take-a-break-web-1.0.0.jar'

& $java --enable-native-access=ALL-UNNAMED `
  -jar $jar `
  --server.port=8081 `
  --spring.datasource.url='jdbc:mysql://localhost:3306/take_a_break_web?createDatabaseIfNotExist=true&useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC' `
  --spring.datasource.username='anokas' `
  --spring.datasource.password='Informatica_1706869' `
  --spring.datasource.driver-class-name='com.mysql.cj.jdbc.Driver'
