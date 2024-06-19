export default defineNuxtRouteMiddleware((to) => {
    if (to?.path.includes("admin") && !useCookie("permitted").value)
        return navigateTo("/");
});
