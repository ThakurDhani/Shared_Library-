def call(String name) {
    // Get current date & time
    def now = new Date()
    def date = now.format("yyyy-MM-dd")
    def time = now.format("HH:mm:ss")
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
