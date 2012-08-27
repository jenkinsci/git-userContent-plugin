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
        if (isMine(fullRepositoryName)) {
            Jenkins.getInstance().checkPermission(Jenkins.ADMINISTER);
            return new ReceivePack(repo.openRepository());
        }

        return null;
    }

    @Override
    public UploadPack createUploadPack(String fullRepositoryName) throws IOException, InterruptedException {
        if (isMine(fullRepositoryName))
            return new UploadPack(repo.openRepository());
        return null;
    }

    private boolean isMine(String name) {
        if (name.startsWith("/"))
            name = name.substring(1);
        return name.equals("userContent.git");
    }
}
