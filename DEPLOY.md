# Deployment

The application is deployed using [Mina](https://github.com/mina-deploy/mina).
Install it with:

    $ bundle install

Setup with (needed only for the first deploy):

    $ mina setup

Then deploy:

    $ mina deploy

Use the `--verbose` option to see a detailed output.

On the very first deploy define the `setup` environment variable so that the
database is properly set up:

    $ mina deploy setup=yes

In order to restart services on deploy, define the `restart` environment
variable:

    $ mina crawler:restart

# Server setup

## Deploy directory

Ensure that `deploy_to` directory is owned by the deploy user and has the right
permissions.

## SSH Authentication

Set up SSH authentication with keys, otherwise `mina` will hang at the password
prompt.
Do not forget to add aliases to you `/etc/hosts` so that server names are
correctly resolved.

## GitHub Authentication

Either generate/copy SSH keys for the deploy machine and upload to Github,
or enable SSH Agent Forwarding.

### References

- [Cache password in GitHub when using HTTPS](https://help.github.com/articles/caching-your-github-password-in-git/)
- [Using SSH Agent Forwarding](https://developer.github.com/guides/using-ssh-agent-forwarding/)

