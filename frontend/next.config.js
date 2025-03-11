/** @type {import('next').NextConfig} */
const nextConfig = {
  reactStrictMode: true,
  swcMinify: true,
  async rewrites() {
    return [
      // Forward requests to the backend via Nginx
      {
        source: '/backend-api/:path*',
        destination: '/api/:path*'
      }
    ];
  },
};

module.exports = nextConfig; 