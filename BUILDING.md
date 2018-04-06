To build **pdfChain**, [Maven][1] must be installed.

Running install without a profile will generate the **pdfChain** jars:
```bash
$ mvn clean install \
    -Dmaven.test.skip=true \
    -Dmaven.javadoc.failOnError=false \
    > >(tee mvn.log) 2> >(tee mvn-error.log >&2)
```

To run the tests, a local blockchain node must be installed. See [Prerequisites](README.md#Prerequisites).
```bash
$ mvn clean install \
    -Dmaven.test.failure.ignore=false \
    -Dmaven.javadoc.failOnError=false \
    > >(tee mvn.log) 2> >(tee mvn-error.log >&2)
```

You can use the supplied `Vagrantfile` to get a [Vagrant][4] VM ([Ubuntu][5] 14.04 LTS - Trusty Tahr, with [VirtualBox][6]) with all the required software installed.
```bash
$ vagrant box add ubuntu/trusty64
$ vagrant up
$ vagrant ssh -- \
    'cd /vagrant ; mvn clean install -Dmaven.test.skip=true -Dmaven.javadoc.failOnError=false' \
    > >(tee mvn.log) 2> >(tee mvn-error.log >&2)
```

[1]: http://maven.apache.org/
[2]: http://www.ghostscript.com/
[3]: http://www.imagemagick.org/
[4]: https://www.vagrantup.com/
[5]: http://www.ubuntu.com/
[6]: https://www.virtualbox.org/