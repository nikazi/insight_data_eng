package(default_visibility = ["//visibility:public"])

java_runtime(
    name = "jdk-9-ea+153",
    srcs = glob(["jdk9-ea+153/**"]),
    java_home = "jdk9-ea+153",
)
java_binary(
	name = "EDGAR",
    srcs = glob(["**/*.java"]),
	deps = [],
	data = [],
    resources = [
    ],
	main_class = "edgar.SessionExtractor",
	
)


