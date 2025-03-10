import './globals.css'
import type { Metadata } from 'next'

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
      <body className="min-h-screen bg-gray-50">
        <header className="bg-white shadow-sm py-4">
          <div className="container mx-auto px-4">
            <h1 className="text-2xl font-bold text-blue-500">Twitter Clone</h1>
          </div>
        </header>
        {children}
      </body>
    </html>
  )
} 