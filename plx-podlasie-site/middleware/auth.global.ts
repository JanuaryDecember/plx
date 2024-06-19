export default defineNuxtRouteMiddleware((to) => {
    const guestPath: string[] = ["/", "/auth/login", "/auth/register"];
    const isLoggedIn: boolean = useCookie("loggedIn").value !== undefined;

    if (isLoggedIn && guestPath.includes(to?.path)) {
        return navigateTo("/user/dashboard");
    }
    if (!isLoggedIn && !guestPath.includes(to?.path)) {
        return navigateTo("/auth/login");
    }
});