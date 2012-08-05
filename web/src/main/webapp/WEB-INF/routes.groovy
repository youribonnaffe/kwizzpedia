
get "/", redirect: "/index.html"
get "/quizz/*", redirect: "/index.html"
get "/api/question/@category", forward: "/question.groovy?category=@category"
