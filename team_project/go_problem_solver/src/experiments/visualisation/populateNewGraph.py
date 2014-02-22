data_filename = raw_input("Name of file containing data: ")
html_filename = raw_input("Name of html template file: ")
html_output_filename = raw_input("Name of output html: ")

template = open(html_filename, "r")
data = open(data_filename, "r")
output = open(html_output_filename, "w")

header = data.readline()
numbers = ""
for line in data:
	numbers += line

for line in template:
	if line[:-1] == "???HEADER???":
		output.write(header)
	elif line[:-1] == "???DATA???":
		output.write(numbers)
	else:
		output.write(line)

output.close()
data.close()
template.close()
