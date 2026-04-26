# Stage 1: Base & Dependency Installation
FROM node:20-alpine AS base
WORKDIR /app
COPY Crypto-Trader-Website/package*.json ./
RUN npm install

# Stage 2: Development (Hot-reloading/Internal testing)
FROM base AS development
COPY Crypto-Trader-Website/ .
EXPOSE 4200
ENTRYPOINT ["npm", "run", "start", "--", "--host", "0.0.0.0"]

# Stage 3: Build for Production
FROM base AS build-prod
COPY Crypto-Trader-Website/ .
RUN npm run build -- --configuration production

# Stage 4: Production Serving (Nginx)
FROM nginx:stable-alpine AS production
COPY --from=build-prod /app/dist/crypto-trader-website /usr/share/nginx/html
# Custom Nginx config to handle Angular routing
COPY Crypto-Trader-Website/nginx.conf /etc/nginx/conf.d/default.conf
EXPOSE 80
CMD ["nginx", "-g", "daemon off;"]
