/** @type {import('next').NextConfig} */
const nextConfig = {
  reactStrictMode: true,
  swcMinify: true,
  async rewrites() {
    // We're not actually rewriting anything because 
    // the Nginx proxy already handles the routing
    return [];
  },
};

module.exports = nextConfig; 