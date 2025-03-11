'use client';

import Link from 'next/link';

export default function AuthLayout({
  children,
}: {
  children: React.ReactNode;
}) {
  return (
    <div className="min-h-screen bg-gray-50">
      <header className="p-4 shadow-sm bg-white">
        <div className="container mx-auto flex justify-between items-center">
          <Link href="/" className="text-2xl font-bold text-blue-500">
            Twitter Clone
          </Link>
        </div>
      </header>
      <main>{children}</main>
    </div>
  );
} 