package sorokin.java.course;

import org.hibernate.SessionFactory;
import org.hibernate.SessionFactoryObserver;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.service.ServiceRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import sorokin.java.course.bank.account.Account;
import sorokin.java.course.users.User;

@Configuration
public class HibernateConfiguration {


    @Bean
    public SessionFactory sessionFactory() {
        try {
            // Создание объекта Configuration.
            org.hibernate.cfg.Configuration configuration = new org.hibernate.cfg.Configuration();

            configuration
                    .addAnnotatedClass(User.class)
                    .addAnnotatedClass(Account.class)
                    .addPackage("sorokin.java.course")
                    .setProperty("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect")
                    .setProperty("hibernate.connection.driver_class", "org.postgresql.Driver")
                    .setProperty("hibernate.connection.url", "jdbc:postgresql://localhost:5432/bank")
                    .setProperty("hibernate.connection.username", "postgres")
                    .setProperty("hibernate.connection.password", "root")
                    .setProperty("hibernate.show_sql", "true")
                    .setProperty("hibernate.current_session_context_class", "thread")
                    .setProperty("hibernate.hbm2ddl.auto", "update");

            // Создание ServiceRegistry из конфигурации Hibernate.
            ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()
                    .applySettings(configuration.getProperties())
                    .build();

            // Создание SessionFactory из ServiceRegistry.
            return configuration
                    .buildSessionFactory(serviceRegistry);
        } catch (Throwable ex) {
            System.err.println("Initial SessionFactory creation failed." + ex);
            throw new ExceptionInInitializerError(ex);
        }

    }

//    private Properties hibernateProperties() {
//        Properties hibernateProperties = new Properties();
//        hibernateProperties.setProperty("hibernate.hbm2ddl.auto", "update");
//        hibernateProperties.setProperty("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect");
//        // Дополнительные свойства Hibernate...
//        return hibernateProperties;
//    }
//
//    private static SessionFactory buildSessionFactory() {
//        try {
//            // Создаем конфигурацию HikariCP
//            HikariConfig hikariConfig = new HikariConfig();
//            hikariConfig.setDriverClassName("org.postgresql.Driver");
//            hikariConfig.setJdbcUrl("jdbc:postgresql://localhost:5432/yourdatabase");
//            hikariConfig.setUsername("yourusername");
//            hikariConfig.setPassword("yourpassword");
//
//            // Создаем DataSource с конфигурацией HikariCP
//            DataSource dataSource = new HikariDataSource(hikariConfig);
//
//            // Создаем объект Configuration Hibernate.
//            Configuration configuration = new Configuration();
//            configuration.addAnnotatedClass(YourEntityClass.class); // Замените YourEntityClass на ваш класс сущности
//
//            // Настройка свойств Hibernate
//            configuration.setProperty("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect");
//            configuration.setProperty("hibernate.show_sql", "true");
//            configuration.setProperty("hibernate.hbm2ddl.auto", "update");
//
//            // Создаем ServiceRegistry, добавляя DataSource
//            ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()
//                    .applySettings(configuration.getProperties())
//                    .applySetting("hibernate.connection.datasource", dataSource)
//                    .build();
//
//            // Создаем SessionFactory
//            return configuration.buildSessionFactory(serviceRegistry);
//        } catch (Throwable ex) {
//            System.err.println("Initial SessionFactory creation failed." + ex);
//            throw new ExceptionInInitializerError(ex);
//        }
}
