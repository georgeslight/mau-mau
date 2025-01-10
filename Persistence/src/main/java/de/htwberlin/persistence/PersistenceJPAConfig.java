package de.htwberlin.persistence;

import de.htwberlin.persistence.exception.DataSourceException;
import de.htwberlin.persistence.exception.EntityManagerFactoryException;
import jakarta.persistence.EntityManagerFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.core.env.Environment;
import org.springframework.dao.annotation.PersistenceExceptionTranslationPostProcessor;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.Objects;
import java.util.Properties;

@Configuration
@PropertySources({
        @PropertySource("classpath:persistence.properties"),
        @PropertySource(value = "classpath:persistence-test.properties", ignoreResourceNotFound = true)
})
@EnableTransactionManagement
@EnableJpaRepositories(basePackages = "de.htwberlin.persistence")
@ComponentScan(basePackages = "de.htwberlin")
public class PersistenceJPAConfig {

    private static final Logger LOGGER = LogManager.getLogger(PersistenceJPAConfig.class);

    @Autowired
    private Environment env;

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
        try{
            LocalContainerEntityManagerFactoryBean factoryBean = new LocalContainerEntityManagerFactoryBean();
            factoryBean.setDataSource(dataSource());
            factoryBean.setPackagesToScan("de.htwberlin");

            HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
            factoryBean.setJpaVendorAdapter(vendorAdapter);
            factoryBean.setEntityManagerFactoryInterface(EntityManagerFactory.class);
            factoryBean.setJpaProperties(additionalProperties());
            return factoryBean;
        } catch (Exception e) {
            LOGGER.error("Failed to configure the EntityManagerFactory: {}", e.getMessage(), e);
            throw new EntityManagerFactoryException("Configuration error for EntityManagerFactory", e);
        }
    }

    @Bean
    public DataSource dataSource() {
        try {
            DriverManagerDataSource dataSource = new DriverManagerDataSource();
            dataSource.setDriverClassName(Objects.requireNonNull(env.getProperty("dataSource.setDriverClassName")));
            dataSource.setUrl(env.getProperty("dataSource.setUrl"));
            dataSource.setUsername(env.getProperty("dataSource.setUsername"));
            dataSource.setPassword(env.getProperty("dataSource.setPassword"));
            return dataSource;
        } catch (Exception e) {
            LOGGER.error("Failed to configure the DataSource: {}", e.getMessage(), e);
            throw new DataSourceException("Configuration error for DataSource", e);
        }
    }

    @Bean
    public PlatformTransactionManager transactionManager(EntityManagerFactory entityManagerFactory) {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(entityManagerFactory);
        return transactionManager;
    }

    private Properties additionalProperties() {
        Properties properties = new Properties();
        properties.setProperty("hibernate.hbm2ddl.auto", env.getProperty("hibernate.hbm2ddl.auto"));
        properties.setProperty("hibernate.show_sql", env.getProperty("hibernate.show_sql"));
        properties.setProperty("hibernate.dialect", env.getProperty("hibernate.dialect"));
        return properties;
    }

    @Bean
    public static PersistenceExceptionTranslationPostProcessor exceptionTranslation() {
        return new PersistenceExceptionTranslationPostProcessor();
    }
}
