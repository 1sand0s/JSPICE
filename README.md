# JSPICE
![GitHub Workflow Status](https://img.shields.io/github/workflow/status/1sand0s/JSPICE/Java%20CI)
![GitHub](https://img.shields.io/github/license/1sand0s/JSPICE)

JSPICE is a [SPICE](http://bwrcs.eecs.berkeley.edu/Classes/IcBook/SPICE/) solver developed from scratch in Java. A SPICE solver is a circuit simulator that can be
used to compute node voltages and branch currents. They are commonly used in PCB/circuit design to ensure the extracted board parasitics/chosen components match the required specifications.

<h2> Supported Analysis </h2>

1. DC Analysis

2. AC Analysis

3. Transient Analysis

<h2>Requirements</h2>

1. [Openjdk 17](https://jdk.java.net/17/)

2. [Maven v3.8.4](https://maven.apache.org/download.cgi)

<h2>Building</h2>


1. <h4>Clone the JSPICE repository:</h4>

    If you have Git installed, clone the repository by executing <br>`git clone https://github.com/1sand0s/JSPICE.git`<br>
    Otherwise, download the repository as a [zip ball](https://github.com/1sand0s/JSPICE/archive/refs/heads/main.zip)

2. <h4>Running with Maven</h4>

    To build and run tests, `cd` into the `JSPICE` directory and execute `mvn clean install`
