This is a general java web project archetype. It depends on SpringBoot(version:2.2.4.RELEASE). Tt includes the following modules:
1.jason-general-jacoco:Aggregate jacoco reports to the module
2.jason-general-web:A general project provide Restful Apis
3.jason-general-common:Provide common jar package
4.jason-general-service:Asynchronous processing for web

Create archetype:
1.mvn archetype:create-from-project
2.cd target\generated-sources\archetype
3.mvn install
4.add by shell or idea