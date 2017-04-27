package org.jenkinsci.plugins.gitUserContent;

import hudson.security.csrf.CrumbFilter;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.Collections;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static org.mockito.Matchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.jvnet.hudson.test.JenkinsRule;

public class GitUserContentCrumbExclusionTest {

    @Rule
    public JenkinsRule r = new JenkinsRule();

    private CrumbFilter filter;
    private HttpServletRequest req;
    private HttpServletResponse resp;
    private FilterChain chain;

    @Before
    public void before() {
        filter = new CrumbFilter();
        req = mock(HttpServletRequest.class);
        resp = mock(HttpServletResponse.class);
        chain = mock(FilterChain.class);
    }

    private void configureTest(final String urlSubstring) throws Exception {
        when(req.getPathInfo()).thenReturn(urlSubstring);
        when(req.getMethod()).thenReturn("POST");
        when(req.getParameterNames()).thenReturn(Collections.<String>emptyEnumeration());
        filter.doFilter(req, resp, chain);
    }

    @Test
    public void testUserContentGit() throws Exception {
        configureTest("/userContent.git");
        verify(resp, never()).sendError(anyInt(), anyString());
    }

    @Test
    public void testUserContentGitNoSlash() throws Exception {
        configureTest("userContent.git");
        verify(resp, times(1)).sendError(anyInt(), anyString());
    }

    @Test
    public void testUserContent() throws Exception {
        configureTest("userContent");
        verify(resp, times(1)).sendError(anyInt(), anyString());
    }

    @Test
    public void testInvalidPath() throws Exception {
        configureTest("/somethingElse/userContent.git");
        verify(resp, times(1)).sendError(anyInt(), anyString());
    }

    @Test
    public void testInvalidNestedPath() throws Exception {
        configureTest("/userContent.git/anotherrepo.git");
        verify(resp, times(1)).sendError(anyInt(), anyString());
    }

    @Test
    public void testInvalidShorterPath() throws Exception {
        when(req.getPathInfo()).thenReturn("/userContent");
        configureTest("/userContent");
        verify(resp, times(1)).sendError(anyInt(), anyString());
    }
}
