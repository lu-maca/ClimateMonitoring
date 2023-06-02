echo "CLIMASCOPE"
echo "------------"
echo "You are launching ClimaScope..."

if not exist bin\data\ (
    robocopy .\data .\bin\data /E
)

cd bin
start javaw -jar ClimateMonitoring.jar