$ErrorActionPreference = 'Stop'

$root = Resolve-Path (Join-Path $PSScriptRoot '..')
$localMaven = Join-Path $root 'tools\apache-maven-3.9.16\bin\mvn.cmd'
$maven = if (Test-Path $localMaven) { $localMaven } else { 'mvn' }
$localRepo = Join-Path $root '.m2\repository'

Push-Location $root
try {
    & $maven "-Dmaven.repo.local=$localRepo" package
}
finally {
    Pop-Location
}
