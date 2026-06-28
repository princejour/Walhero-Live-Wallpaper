# Build a signed release from phone

For OPPO App Market you need a signed release APK or AAB.

Basic workflow:

1. Keep your keystore file private.
2. Add signing values as GitHub repository secrets.
3. Use a release workflow that reads the secrets and builds the release file.
4. Do not commit the keystore file to a public repository.

Keep the same keystore for future app updates.
