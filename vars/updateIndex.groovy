def call(String name) {
    // Get current date & time
    def now = new Date()
    def tz = TimeZone.getTimeZone("Asia/Kolkata")
    def date = now.format("yyyy-MM-dd", tz)
    def time = now.format("HH:mm:ss", tz)
    // Load HTML template from resources
    def template = libraryResource('index.html')
    // Replace placeholders
    template = template
        .replace('${name}', name)
        .replace('${date}', date)
        .replace('${time}', time)
    // Write the final index.html into the workspace
    writeFile file: 'index.html', text: template
    echo "index.html generated for ${name} at ${date} ${time}"
}
