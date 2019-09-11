package ru.cleverhause.device.api.filters;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.RequestMatcher;
import ru.cleverhause.device.api.dto.DeviceControl;
import ru.cleverhause.device.api.dto.DeviceData;
import ru.cleverhause.device.api.dto.DeviceStructure;
import ru.cleverhause.device.api.dto.request.BoardRequestBody;
import ru.cleverhause.device.api.filters.mapper.AbstractRequestBodyToObjectMapper;
import ru.cleverhause.device.api.filters.mapper.RequestBodyToObjectMapperFactory;
import ru.cleverhause.device.api.service.BoardDataService;

import javax.servlet.FilterChain;
import javax.servlet.ReadListener;
import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;

/**
 * Created by
 *
 * @author Aleksandr_Ivanov1
 * @date 8/1/2018.
 */
public class BoardHttpBasicAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

    private static Logger LOGGER = LoggerFactory.getLogger(BoardHttpBasicAuthenticationFilter.class);

    enum DtoClass {
        CONTROL(DeviceControl.class),
        DATA(DeviceData.class),
        STRUCTURE(DeviceStructure.class);

        private Class<? extends Serializable> dtoClass;

        DtoClass(Class<? extends Serializable> dtoClass) {
            this.dtoClass = dtoClass;
        }

        public Class<? extends Serializable> getClazz() {
            return dtoClass;
        }
    }

    @Autowired
    private BoardDataService boardDataService;

    @Autowired
    private RequestBodyToObjectMapperFactory requestBodyToObjectMapperFactory;

    private boolean needCheckBoardBelongsToUser = true;

    private BoardRequestBody body;

    public BoardHttpBasicAuthenticationFilter(String defaultFilterProcessesUrl) {
        super(defaultFilterProcessesUrl);
    }

    public BoardHttpBasicAuthenticationFilter(RequestMatcher requiresAuthenticationRequestMatcher) {
        super(requiresAuthenticationRequestMatcher);
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
        CustomHttpServletRequestWrapper requestWrapper = new CustomHttpServletRequestWrapper((HttpServletRequest) req);
        super.doFilter(requestWrapper, res, chain);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        super.successfulAuthentication(request, response, chain, authResult);
        chain.doFilter(request, response);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {
        Authentication authResult = null;
        String tokens[] = getTokens(request);
        UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(tokens[0], tokens[1]);
        boolean doesBoardBelongToUser = false;
        if (needCheckBoardBelongsToUser && boardDataService != null) {
            doesBoardBelongToUser = boardDataService.checkBoardNumber(body.getBoardUID(), tokens[0]);
        }

        if (getAuthenticationManager() != null && (doesBoardBelongToUser || !needCheckBoardBelongsToUser)) {
            authResult = this.getAuthenticationManager().authenticate(authRequest);
        } else {
            throw new RuntimeException("No AuthenticationManager found for authenticating requests from board");
        }

        return authResult;
    }

    private String[] getTokens(HttpServletRequest request) {
        String[] tokens = null;
        if (retrieveBodyFromPostRequest(request)) {
            tokens = this.extractCreds();
        }

        return tokens;
    }

    private boolean retrieveBodyFromPostRequest(HttpServletRequest request) {
        if (HttpMethod.POST.matches(request.getMethod())) {
            try {
                this.body = tryMapFrom(request);
            } catch (Exception e) {
                LOGGER.error("Can't convert request input stream to json", e); //TODO
            }
        }

        return body != null;
    }

    private BoardRequestBody tryMapFrom(HttpServletRequest request) throws IOException {
        AbstractRequestBodyToObjectMapper mapper;
        for (DtoClass dtoClass : DtoClass.values()) {
            mapper = requestBodyToObjectMapperFactory.create(dtoClass.getClazz());
            try {
                return mapper.map(request);
            } catch (Exception e) {
                LOGGER.info("Mapper {} couldn't been used for that request", mapper);
            }
        }

        return null;
    }

    public void isNeedCheckBoardBelongsToUser(boolean needCheckBoardBelongsToUser) {
        this.needCheckBoardBelongsToUser = needCheckBoardBelongsToUser;
    }

    private String[] extractCreds() {
        String username = this.body.getUsername();
        String password = this.body.getPassword();

        return new String[]{username, password};
    }

    private class CustomHttpServletRequestWrapper extends HttpServletRequestWrapper {

        private final String body;

        public CustomHttpServletRequestWrapper(HttpServletRequest request) {
            super(request);

            StringBuilder stringBuilder = new StringBuilder();
            BufferedReader bufferedReader = null;

            try {
                InputStream inputStream = request.getInputStream();

                if (inputStream != null) {
                    bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

                    char[] charBuffer = new char[128];
                    int bytesRead = -1;

                    while ((bytesRead = bufferedReader.read(charBuffer)) > 0) {
                        stringBuilder.append(charBuffer, 0, bytesRead);
                    }
                } else {
                    stringBuilder.append("");
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            } finally {
                if (bufferedReader != null) {
                    try {
                        bufferedReader.close();
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
            }

            body = stringBuilder.toString();
        }

        @Override
        public ServletInputStream getInputStream() throws IOException {
            final ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(body.getBytes());

            ServletInputStream inputStream = new ServletInputStream() {
                @Override
                public boolean isFinished() {
                    return false;
                }

                @Override
                public boolean isReady() {
                    return false;
                }

                @Override
                public void setReadListener(ReadListener readListener) {

                }

                public int read() throws IOException {
                    return byteArrayInputStream.read();
                }
            };

            return inputStream;
        }
    }
}