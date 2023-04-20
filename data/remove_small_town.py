new_list = []

with open("monitoring_coordinates.csv", "r") as f:
    lines = f.readlines()
    for line in lines:
        splitted = line.split(";")
        try:
            if int(splitted[-1]) > 10000:
                new_list.append(line)
        except:
            pass

with open("prova.csv", "w") as fw:
    fw.writelines(new_list)
