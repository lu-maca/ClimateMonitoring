echo "CLIMASCOPE"
echo "------------"
echo "You are launching ClimaScope..."

if [ ! -d "bin/data" ]; then
    cp -r data bin/data
fi

cd bin
java -jar ClimateMonitoring.jar