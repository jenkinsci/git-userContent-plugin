package org.jenkinsci.plugins.gitUserContent;

import hudson.Extension;
import jenkins.model.Jenkins;
import org.eclipse.jgit.transport.ReceivePack;
import org.eclipse.jgit.transport.UploadPack;
import org.jenkinsci.plugins.gitserver.RepositoryResolver;

import javax.inject.Inject;
import java.io.IOException;

/**
 * Exposes this repository over SSH.
 *
 * @author Kohsuke Kawaguchi
 */
@Extension
public class GitUserContentRepositorySSHAccess extends RepositoryResolver {
    @Inject
    GitUserContentRepository repo;

    @Override
    public ReceivePack createReceivePack(String fullRepositoryName) throws IOException, InterruptedException {
        if (isMine(fullRepositoryName))
            return repo.createReceivePack(repo.openRepository());
        return null;
    }

    @Override
    public UploadPack createUploadPack(String fullRepositoryName) throws IOException, InterruptedException {
        if (isMine(fullRepositoryName))
            return new UploadPack(repo.openRepository());
        return null;
    }

    private boolean isMine(String name) {
        // Depending on the Git URL the client uses, we may or may not get leading '/'.
        // For example, server:userContent.git vs ssh://server/userContent.git
        if (name.startsWith("/"))
            name = name.substring(1);
        return name.equals("userContent.git");
    }
}
