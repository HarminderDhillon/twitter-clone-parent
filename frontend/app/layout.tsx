import './globals.css'
import type { Metadata } from 'next'
import Link from 'next/link'
import { Inter } from 'next/font/google'

const inter = Inter({ subsets: ['latin'] })

export const metadata: Metadata = {
  title: 'Twitter Clone',
  description: 'A Twitter clone application built with Next.js and Spring Boot',
}

export default function RootLayout({
  children,
}: {
  children: React.ReactNode
}) {
  return (
    <html lang="en">
      <body className={`${inter.className} antialiased min-h-screen`}>
        <header className="bg-white border-b border-gray-200 py-4">
          <div className="container max-w-6xl mx-auto px-4 flex justify-between items-center">
            <div className="flex items-center space-x-4">
              <Link href="/" className="text-xl font-bold text-blue-500">Twitter Clone</Link>
            </div>
            <div className="flex items-center space-x-4">
              <Link href="/auth/login" className="text-gray-600 hover:text-blue-500">Login</Link>
              <Link href="/auth/signup" className="bg-blue-500 hover:bg-blue-600 text-white px-4 py-2 rounded-full">Sign Up</Link>
              <Link href="/api-test" className="text-xs text-gray-400 hover:text-gray-600">API Test</Link>
            </div>
          </div>
        </header>
        {children}
      </body>
    </html>
  )
} 