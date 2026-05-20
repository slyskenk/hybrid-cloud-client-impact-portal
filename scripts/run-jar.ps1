param(
    [int] $Port = 8080
)

$ErrorActionPreference = 'Stop'

$root = Resolve-Path (Join-Path $PSScriptRoot '..')
$jar = Join-Path $root 'target\hybrid-cloud-client-impact-portal-0.1.0-SNAPSHOT.jar'

if (-not (Test-Path $jar)) {
    throw "JAR not found. Run scripts\build.ps1 first."
}

Push-Location $root
try {
    & java -jar $jar "--server.port=$Port"
}
finally {
    Pop-Location
}
