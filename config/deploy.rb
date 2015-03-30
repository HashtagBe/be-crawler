require 'mina/bundler'
require 'mina/git'

# Basic settings:
#   domain       - The hostname to SSH to.
#   deploy_to    - Path to deploy into.
#   repository   - Git repo to clone from. (needed by mina/git)
#   branch       - Branch name to deploy. (needed by mina/git)

# Optional settings:
#   set :user, 'foobar'    # Username in the server to SSH to.
#   set :port, '30000'     # SSH port number.
#   set :forward_agent, true     # SSH forward_agent.

set :domain, 'srv-hashtag-content1'
set :port, 40022
set :user, 'hashtagbe'
set :deploy_to, "/home/#{user}/be-crawler"
set :repository, 'git@github.com:HashtagBe/be-crawler.git'
set :branch, 'master'
set :forward_agent, true

# Manually create these paths in shared/ (eg: shared/config/database.yml) in your server.
# They will be linked in the 'deploy:link_shared_paths' step.
set :shared_paths, ['db', 'logs', 'repo']

# This task is the environment that is loaded for most commands, such as
# `mina deploy` or `mina rake`.
task :environment do
  # nothing to do for the moment.
end

# Put any custom mkdir's in here for when `mina setup` is ran.
task :setup => :environment do
  shared_paths.each do |path|
    queue! %[mkdir -p "#{deploy_to}/#{shared_path}/#{path}"]
    queue! %[chmod g+rx,u+rwx "#{deploy_to}/#{shared_path}/#{path}"]
  end
end

desc "Deploys the current version to the server."
task :deploy => :environment do
  deploy do
    # Put things that will set up an empty directory into a fully set-up
    # instance of your project.
    invoke :'git:clone'
    invoke :'deploy:link_shared_paths'
    invoke :'mvn:package'
    invoke :'deploy:cleanup'

    to :launch do
      invoke :'mvn:setup' if ENV['setup']
      invoke :'crawler:jdbc'
      invoke :'crawler:restart'
    end
  end
end

# Custom tasks

namespace :mvn do
  desc "Package the application with maven"
  task :package do
    queue %{echo "-----> Packaging ..."}
    queue echo_cmd %[mvn package]
  end
  desc "Run maven tasks required to set up the project"
  task :setup do
    queue %{echo "-----> Setting up ..."}
    queue echo_cmd %[mvn sql:execute]
  end
end

namespace :crawler do
  set :logfile, "#{deploy_to}/#{shared_path}/logs/nohup.log"
  set :pidfile, "#{deploy_to}/#{shared_path}/logs/run.pid"
  set :current_dir, "#{deploy_to}/#{current_path}"

  desc "Fix the jdbc connection url"
  task :jdbc do
    queue %{echo "-----> Fixing jdbc url"}
    config = "#{current_dir}/WebContent/WEB-INF/classes/mybatis/jdbc.properties"
    database = "#{deploy_to}/#{shared_path}/db/crawled-data"
    queue echo_cmd %[sed -i -r 's;url=(.*);url=jdbc:h2:#{database};g' #{config}]
  end

  desc "Start the application in background using `nohup`"
  task :start do
    queue %{echo "-----> Starting ..."}
    in_directory current_dir do
      queue echo_cmd %[nohup sh bin/webapp > #{logfile} 2>&1 & echo $! > #{pidfile}]
    end
    queue %{sleep 10} # just to ensure it started correctly
  end

  desc "Stop the application"
  task :stop do
    queue %{echo "-----> Stopping ..."}
    queue echo_cmd %[test -f #{pidfile} && kill `cat #{pidfile}` || true]
    queue %{sleep 1} # just to ensure it stopped
  end

  desc "Restart the application"
  task :restart => [:stop, :run]

  desc "Start crawling"
  task :run => :start do
    queue %{echo "-----> Start crawling"}
    queue echo_cmd %[curl -s localhost:8088/article/startArticleCrawler.do > /dev/null && echo "ok" || echo "error"]
  end

  desc "Check if the crawler is running"
  task :status do
    queue %{echo "-----> Checking crawler status"}
    queue echo_cmd %[curl -s localhost:8088 > /dev/null && echo "running" || echo "not running"]
  end

end
