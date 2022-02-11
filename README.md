# overview

This project is made up of keycloak plugins(spi).User and role syncronization is the main business to integrate different authentication or authorization in micro-services.Tutorials can be found here:[building-an-event-listener-spi-plugin-for-keycloak](https://medium.com/@adwaitthattey/building-an-event-listener-spi-plugin-for-keycloak-5bf9de1b0965) and [Callback (hook) on User creation](https://keycloak.discourse.group/t/callback-hook-on-user-creation/5259)

## FileBrowser

[FileBrowser](https://filebrowser.org/) provide a proxy authentication method,which has been done by kubernetes ingress and oauth2-proxy.But when use proxy authtication method,there should be users in FileBrowser database,which is actually in keycloak.When create user event is launched in keycloak,we do things below:

* create user in filebrowser
* add default permissions to user
* set user scope to `wellspiking-training/[username]`