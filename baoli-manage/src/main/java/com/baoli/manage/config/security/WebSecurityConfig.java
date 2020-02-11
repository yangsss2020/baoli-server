package com.baoli.manage.config.security;

import com.baoli.manage.sms.service.impl.SecurityDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.ObjectPostProcessor;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;

import javax.sql.DataSource;

/**
 * @author ys
 * @create 2020-02-11-0:00
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private SelfFilterInvocationSecurityMetadataSource filterInvocationSecurityMetadataSource; //获取需要拦截的权限

    @Autowired
    private SelfAccessDecisionManager accessDecisionManager; //判断权限
    @Autowired
    private UrlAuthenticationSuccessHandler authenticationSuccessHandler;  //登陆成功
    @Autowired
    private UrlLogoutSuccessHandler logoutSuccessHandler;
    @Autowired
    private UrlAuthenticationFailureHandler authenticationFailureHandler;

    @Autowired
    private SecurityDetailService securityDetailService;  //自定义登陆
    @Autowired
    private UrlAuthenticationEntryPoint authenticationEntryPoint; //用户未登录
    @Autowired
    private UrlAccessDeniedHandler accessDeniedHandler;
    @Bean
    public BCryptPasswordEncoder getPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
    @Autowired
    private DataSource dataSource;
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(securityDetailService).passwordEncoder(getPasswordEncoder()); //自定义登陆
    }

    @Override
    protected void configure(HttpSecurity security) throws Exception {
        JdbcTokenRepositoryImpl repository = new JdbcTokenRepositoryImpl();
        repository.setDataSource(dataSource);
        security.csrf().disable(); //关闭csrf跨站伪造
        // 未登录时：返回状态码401
        security.exceptionHandling().authenticationEntryPoint(authenticationEntryPoint);
        // 无权访问时：返回状态码403
        security.exceptionHandling().accessDeniedHandler(accessDeniedHandler);
        //url权限认证
        security.antMatcher("/**").authorizeRequests()
//                .antMatchers("/security/user/**").hasRole("ADMIN") //需要ADMIN角色才可以访问
//                .antMatchers("/connect").hasIpAddress("127.0.0.1") //只有ip[127.0.0.1]可以访问'/connect'接口
                .anyRequest() //其他任何请求
                .authenticated() //都需要身份认证
                .withObjectPostProcessor(new ObjectPostProcessor<FilterSecurityInterceptor>() {
                    @Override
                    public <O extends FilterSecurityInterceptor> O postProcess(O o) {
                        o.setSecurityMetadataSource(filterInvocationSecurityMetadataSource); //动态获取url权限配置
                        o.setAccessDecisionManager(accessDecisionManager); //权限判断
                        return o;
                    }
                });
        security
                .formLogin()
                .loginProcessingUrl("/login")
                .usernameParameter("username")
                .passwordParameter("password")
                .failureHandler(authenticationFailureHandler) //登陆失败
                .successHandler(authenticationSuccessHandler)  //登陆成功
                .and()
                .rememberMe()
                .alwaysRemember(true)
                .tokenRepository(repository);
        security
                .logout()
                .logoutUrl("/logout")
                .logoutSuccessHandler(logoutSuccessHandler);
    }
}
